package com.sim.shopping.application.shipment;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.domain.common.exception.OrderException;
import com.sim.shopping.domain.event.ShipmentCreatedEvent;
import com.sim.shopping.infrastructure.persistence.entity.LogisticsRecordDO;
import com.sim.shopping.infrastructure.persistence.entity.LogisticsTrackDO;
import com.sim.shopping.infrastructure.persistence.entity.OrderDO;
import com.sim.shopping.infrastructure.persistence.entity.ShipmentDO;
import com.sim.shopping.infrastructure.persistence.mapper.LogisticsRecordMapper;
import com.sim.shopping.infrastructure.persistence.mapper.LogisticsTrackMapper;
import com.sim.shopping.infrastructure.persistence.mapper.OrderMapper;
import com.sim.shopping.infrastructure.persistence.mapper.ShipmentMapper;
import com.sim.shopping.interfaces.dto.shipment.LogisticsResponse;
import com.sim.shopping.interfaces.dto.shipment.LogisticsTrackItem;
import com.sim.shopping.interfaces.dto.shipment.CreateShipmentRequest;
import com.sim.shopping.interfaces.dto.shipment.ShipmentResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ShipmentService {

    private final ShipmentMapper shipmentMapper;
    private final LogisticsRecordMapper logisticsRecordMapper;
    private final LogisticsTrackMapper logisticsTrackMapper;
    private final OrderMapper orderMapper;
    private final ApplicationEventPublisher eventPublisher;

    private static final String ORDER_STATUS_PAID = "PAID";
    private static final String ORDER_STATUS_SHIPPED = "SHIPPED";
    private static final String SHIPMENT_STATUS_SHIPPED = "SHIPPED";
    private static final String LOGISTICS_STATUS_CREATED = "CREATED";

    public ShipmentService(ShipmentMapper shipmentMapper,
                            LogisticsRecordMapper logisticsRecordMapper,
                            LogisticsTrackMapper logisticsTrackMapper,
                            OrderMapper orderMapper,
                            ApplicationEventPublisher eventPublisher) {
        this.shipmentMapper = shipmentMapper;
        this.logisticsRecordMapper = logisticsRecordMapper;
        this.logisticsTrackMapper = logisticsTrackMapper;
        this.orderMapper = orderMapper;
        this.eventPublisher = eventPublisher;
    }

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

    public ShipmentResponse getShipmentByOrderNo(String orderNo) {
        LambdaQueryWrapper<ShipmentDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShipmentDO::getOrderNo, orderNo);
        ShipmentDO shipment = shipmentMapper.selectOne(wrapper);
        if (shipment == null) {
            return null;
        }
        return convertToShipmentResponse(shipment);
    }

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

    private String generateShipmentNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = randomAlphanumeric(4);
        return "SH" + timestamp + random;
    }

    private String randomAlphanumeric(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
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
}
