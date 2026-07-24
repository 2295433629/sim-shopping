package com.sim.shopping.application.shipment;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.domain.common.exception.OrderException;
import com.sim.shopping.infrastructure.common.OrderNoGenerator;
import com.sim.shopping.domain.event.ShipmentCreatedEvent;
import com.sim.shopping.infrastructure.persistence.entity.LogisticsRecordDO;
import com.sim.shopping.infrastructure.persistence.entity.LogisticsTrackDO;
import com.sim.shopping.infrastructure.persistence.entity.OrderDO;
import com.sim.shopping.infrastructure.persistence.entity.ShipmentDO;
import com.sim.shopping.infrastructure.persistence.entity.ShopDO;
import com.sim.shopping.infrastructure.persistence.mapper.LogisticsRecordMapper;
import com.sim.shopping.infrastructure.persistence.mapper.LogisticsTrackMapper;
import com.sim.shopping.infrastructure.persistence.mapper.OrderMapper;
import com.sim.shopping.infrastructure.persistence.mapper.ShipmentMapper;
import com.sim.shopping.infrastructure.persistence.mapper.ShopMapper;
import com.sim.shopping.interfaces.dto.shipment.LogisticsResponse;
import com.sim.shopping.interfaces.dto.shipment.LogisticsTrackItem;
import com.sim.shopping.interfaces.dto.shipment.CreateShipmentRequest;
import com.sim.shopping.interfaces.dto.shipment.ShipmentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * 物流服务，处理物流信息查询、自动发货和状态推进
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Service
public class ShipmentService {

    private static final Logger log = LoggerFactory.getLogger(ShipmentService.class);

    private final ShipmentMapper shipmentMapper;
    private final LogisticsRecordMapper logisticsRecordMapper;
    private final LogisticsTrackMapper logisticsTrackMapper;
    private final OrderMapper orderMapper;
    private final ShopMapper shopMapper;
    private final ApplicationEventPublisher eventPublisher;

    private static final String ORDER_STATUS_PAID = "PAID";
    private static final String ORDER_STATUS_SHIPPED = "SHIPPED";
    private static final String SHIPMENT_STATUS_SHIPPED = "SHIPPED";
    private static final String LOGISTICS_STATUS_CREATED = "CREATED";

    private static final List<String> LOGISTICS_COMPANIES = List.of(
            "顺丰快递", "圆通快递", "中通快递", "韵达快递", "申通快递", "京东物流"
    );

    public ShipmentService(ShipmentMapper shipmentMapper,
                            LogisticsRecordMapper logisticsRecordMapper,
                            LogisticsTrackMapper logisticsTrackMapper,
                            OrderMapper orderMapper,
                            ShopMapper shopMapper,
                            ApplicationEventPublisher eventPublisher) {
        this.shipmentMapper = shipmentMapper;
        this.logisticsRecordMapper = logisticsRecordMapper;
        this.logisticsTrackMapper = logisticsTrackMapper;
        this.orderMapper = orderMapper;
        this.shopMapper = shopMapper;
        this.eventPublisher = eventPublisher;
    }

    /**
     * 创建Shipment
     * @param shopId shopId
     * @param req req
     * @return 返回结果
     */
    @Transactional
    public ShipmentResponse createShipment(Long shopId, CreateShipmentRequest req) {
        // 查找订单并验证
        OrderDO order = orderByNo(req.getOrderNo());
        if (order == null) {
            throw new OrderException.OrderNotFoundException("订单不存在: " + req.getOrderNo());
        }
        if (!order.getShopId().equals(shopId)) {
            throw new BusinessException(403, "订单不属于该店铺");
        }
        if (!ORDER_STATUS_PAID.equals(order.getStatus())) {
            throw new OrderException.OrderStatusException("只能对已支付的订单进行发货");
        }

        // 检查是否已有发货单
        LambdaQueryWrapper<ShipmentDO> existShipmentWrapper = new LambdaQueryWrapper<>();
        existShipmentWrapper.eq(ShipmentDO::getOrderNo, req.getOrderNo());
        if (shipmentMapper.selectCount(existShipmentWrapper) > 0) {
            throw new BusinessException(400, "该订单已发货，不可重复发货");
        }

        // 生成发货单号
        String shipmentNo = generateShipmentNo();

        // 创建发货单
        ShipmentDO shipment = new ShipmentDO();
        shipment.setOrderId(order.getId());
        shipment.setOrderNo(order.getOrderNo());
        shipment.setShopId(shopId);
        shipment.setShipmentNo(shipmentNo);
        shipment.setLogisticsCompany(req.getLogisticsCompany());
        shipment.setStatus(SHIPMENT_STATUS_SHIPPED);
        shipment.setShippedAt(LocalDateTime.now());
        shipmentMapper.insert(shipment);

        // 更新订单状态为SHIPPED
        order.setStatus(ORDER_STATUS_SHIPPED);
        order.setShippedAt(LocalDateTime.now());
        orderMapper.updateById(order);

        // 创建物流记录
        LogisticsRecordDO logisticsRecord = new LogisticsRecordDO();
        logisticsRecord.setOrderId(order.getId());
        logisticsRecord.setOrderNo(order.getOrderNo());
        logisticsRecord.setShipmentId(shipment.getId());
        logisticsRecord.setTrackingNo(shipmentNo);
        logisticsRecord.setLogisticsCompany(req.getLogisticsCompany());
        logisticsRecord.setStatus(LOGISTICS_STATUS_CREATED);

        // 从 shop 表读取发货地址
        ShopDO shop = shopMapper.selectById(shopId);
        if (shop != null) {
            logisticsRecord.setSenderCity(shop.getSenderCity());
            String fullSenderAddress = buildFullAddress(shop.getSenderProvince(), shop.getSenderCity(),
                    shop.getSenderDistrict(), shop.getSenderAddress());
            logisticsRecord.setSenderAddress(fullSenderAddress);
        }
        logisticsRecord.setReceiverAddress(order.getReceiverAddress());

        logisticsRecordMapper.insert(logisticsRecord);

        // 创建初始物流轨迹
        LogisticsTrackDO track = new LogisticsTrackDO();
        track.setLogisticsId(logisticsRecord.getId());
        track.setStatus(LOGISTICS_STATUS_CREATED);
        track.setDescription("已创建物流单");
        track.setLocation(null);
        track.setOccurredAt(LocalDateTime.now());
        logisticsTrackMapper.insert(track);

        // 发布发货事件
        eventPublisher.publishEvent(new ShipmentCreatedEvent(
                order.getOrderNo(), shipment.getId(), shipmentNo, req.getLogisticsCompany()));

        // 构建返回
        return convertToShipmentResponse(shipment);
    }

    /**
     * auto Ship Order
     * @param orderNo orderNo
     */
    @Transactional
    public void autoShipOrder(String orderNo) {
        OrderDO order = orderByNo(orderNo);
        if (order == null) {
            return;
        }
        if (!ORDER_STATUS_PAID.equals(order.getStatus())) {
            return;
        }

        // 检查是否已有发货单
        LambdaQueryWrapper<ShipmentDO> existShipmentWrapper = new LambdaQueryWrapper<>();
        existShipmentWrapper.eq(ShipmentDO::getOrderNo, orderNo);
        if (shipmentMapper.selectCount(existShipmentWrapper) > 0) {
            return;
        }

        // 随机选择物流公司
        ThreadLocalRandom random = ThreadLocalRandom.current();
        String logisticsCompany = LOGISTICS_COMPANIES.get(random.nextInt(LOGISTICS_COMPANIES.size()));

        // 生成运单号
        String trackingNo = generateTrackingNo(logisticsCompany);

        // 生成发货单号
        String shipmentNo = generateShipmentNo();

        // 创建发货单
        ShipmentDO shipment = new ShipmentDO();
        shipment.setOrderId(order.getId());
        shipment.setOrderNo(order.getOrderNo());
        shipment.setShopId(order.getShopId());
        shipment.setShipmentNo(shipmentNo);
        shipment.setLogisticsCompany(logisticsCompany);
        shipment.setStatus(SHIPMENT_STATUS_SHIPPED);
        shipment.setShippedAt(LocalDateTime.now());
        shipmentMapper.insert(shipment);

        // 更新订单状态为SHIPPED
        order.setStatus(ORDER_STATUS_SHIPPED);
        order.setShippedAt(LocalDateTime.now());
        orderMapper.updateById(order);

        // 创建物流记录
        LogisticsRecordDO logisticsRecord = new LogisticsRecordDO();
        logisticsRecord.setOrderId(order.getId());
        logisticsRecord.setOrderNo(order.getOrderNo());
        logisticsRecord.setShipmentId(shipment.getId());
        logisticsRecord.setTrackingNo(trackingNo);
        logisticsRecord.setLogisticsCompany(logisticsCompany);
        logisticsRecord.setStatus(LOGISTICS_STATUS_CREATED);

        // 从 shop 表读取发货地址
        ShopDO shop = shopMapper.selectById(order.getShopId());
        if (shop != null) {
            logisticsRecord.setSenderCity(shop.getSenderCity());
            String fullSenderAddress = buildFullAddress(shop.getSenderProvince(), shop.getSenderCity(),
                    shop.getSenderDistrict(), shop.getSenderAddress());
            logisticsRecord.setSenderAddress(fullSenderAddress);
        }
        logisticsRecord.setReceiverAddress(order.getReceiverAddress());

        logisticsRecordMapper.insert(logisticsRecord);

        // 创建初始物流轨迹
        LogisticsTrackDO track = new LogisticsTrackDO();
        track.setLogisticsId(logisticsRecord.getId());
        track.setStatus(LOGISTICS_STATUS_CREATED);
        track.setDescription("已创建物流单");
        track.setLocation(null);
        track.setOccurredAt(LocalDateTime.now());
        logisticsTrackMapper.insert(track);

        // 发布发货事件
        eventPublisher.publishEvent(new ShipmentCreatedEvent(
                order.getOrderNo(), shipment.getId(), trackingNo, logisticsCompany));

        log.info("自动发货完成: orderNo={}, logisticsCompany={}, trackingNo={}",
                orderNo, logisticsCompany, trackingNo);
    }

    /**
     * 获取Shipment By Order No
     * @param orderNo orderNo
     * @return 返回结果
     */
    public ShipmentResponse getShipmentByOrderNo(String orderNo) {
        LambdaQueryWrapper<ShipmentDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShipmentDO::getOrderNo, orderNo);
        ShipmentDO shipment = shipmentMapper.selectOne(wrapper);
        if (shipment == null) {
            return null;
        }
        return convertToShipmentResponse(shipment);
    }

    /**
     * 获取Logistics By Order No
     * @param orderNo orderNo
     * @return 返回结果
     */
    public LogisticsResponse getLogisticsByOrderNo(String orderNo) {
        LambdaQueryWrapper<LogisticsRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LogisticsRecordDO::getOrderNo, orderNo);
        LogisticsRecordDO record = logisticsRecordMapper.selectOne(wrapper);
        if (record == null) {
            return null;
        }

        // 查询物流轨迹
        LambdaQueryWrapper<LogisticsTrackDO> trackWrapper = new LambdaQueryWrapper<>();
        trackWrapper.eq(LogisticsTrackDO::getLogisticsId, record.getId());
        trackWrapper.orderByDesc(LogisticsTrackDO::getOccurredAt);
        List<LogisticsTrackDO> tracks = logisticsTrackMapper.selectList(trackWrapper);

        LogisticsResponse response = new LogisticsResponse();
        response.setId(record.getId());
        response.setOrderNo(record.getOrderNo());
        response.setTrackingNo(record.getTrackingNo());
        response.setLogisticsCompany(record.getLogisticsCompany());
        response.setStatus(record.getStatus());
        response.setDeliveredAt(record.getDeliveredAt());
        response.setSenderAddress(record.getSenderAddress());
        response.setReceiverAddress(record.getReceiverAddress());
        response.setSenderCity(record.getSenderCity());

        List<LogisticsTrackItem> trackItems = tracks.stream().map(t -> {
            LogisticsTrackItem item = new LogisticsTrackItem();
            item.setStatus(t.getStatus());
            item.setDescription(t.getDescription());
            item.setLocation(t.getLocation());
            item.setOccurredAt(t.getOccurredAt());
            return item;
        }).collect(Collectors.toList());
        response.setTracks(trackItems);

        return response;
    }

    // ========== Private helper methods ==========

    private OrderDO orderByNo(String orderNo) {
        LambdaQueryWrapper<OrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDO::getOrderNo, orderNo);
        return orderMapper.selectOne(wrapper);
    }

    private String generateTrackingNo(String logisticsCompany) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private String generateShipmentNo() {
        return OrderNoGenerator.generateOrderNo("SH");
    }

    private ShipmentResponse convertToShipmentResponse(ShipmentDO shipment) {
        ShipmentResponse resp = new ShipmentResponse();
        resp.setShipmentId(shipment.getId());
        resp.setOrderNo(shipment.getOrderNo());
        resp.setShipmentNo(shipment.getShipmentNo());
        resp.setLogisticsCompany(shipment.getLogisticsCompany());
        resp.setTrackingNo(shipment.getShipmentNo());
        resp.setStatus(shipment.getStatus());
        resp.setShippedAt(shipment.getShippedAt());
        return resp;
    }

    private String buildFullAddress(String province, String city, String district, String address) {
        StringBuilder sb = new StringBuilder();
        if (province != null && !province.isEmpty()) sb.append(province);
        if (city != null && !city.isEmpty()) sb.append(city);
        if (district != null && !district.isEmpty()) sb.append(district);
        if (address != null && !address.isEmpty()) sb.append(address);
        return sb.length() > 0 ? sb.toString() : null;
    }
}
