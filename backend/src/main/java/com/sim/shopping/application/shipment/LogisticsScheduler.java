package com.sim.shopping.application.shipment;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sim.shopping.application.refund.RefundService;
import com.sim.shopping.domain.event.LogisticsDeliveredEvent;
import com.sim.shopping.infrastructure.persistence.entity.LogisticsRecordDO;
import com.sim.shopping.infrastructure.persistence.entity.LogisticsTrackDO;
import com.sim.shopping.infrastructure.persistence.entity.OrderDO;
import com.sim.shopping.infrastructure.persistence.entity.RefundDO;
import com.sim.shopping.infrastructure.persistence.mapper.LogisticsRecordMapper;
import com.sim.shopping.infrastructure.persistence.mapper.LogisticsTrackMapper;
import com.sim.shopping.infrastructure.persistence.mapper.OrderMapper;
import com.sim.shopping.infrastructure.persistence.mapper.RefundMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class LogisticsScheduler {

    private static final Logger log = LoggerFactory.getLogger(LogisticsScheduler.class);

    private final LogisticsRecordMapper logisticsRecordMapper;
    private final LogisticsTrackMapper logisticsTrackMapper;
    private final OrderMapper orderMapper;
    private final RefundMapper refundMapper;
    private final ShipmentService shipmentService;
    private final RefundService refundService;
    private final ApplicationEventPublisher eventPublisher;

    @Value("${mock.logistics.interval:10800}")
    private int logisticsIntervalSeconds; // 默认10800秒 = 3小时

    private static final String ORDER_STATUS_DELIVERED = "DELIVERED";

    /**
     * 物流状态推进顺序: CREATED -> PICKED_UP -> SORTING -> IN_TRANSIT -> OUT_FOR_DELIVERY -> DELIVERED
     */
    private static final List<String> STATUS_FLOW = Arrays.asList(
            "CREATED", "PICKED_UP", "SORTING", "IN_TRANSIT", "OUT_FOR_DELIVERY", "DELIVERED"
    );

    /**
     * 每个状态对应的基础轨迹描述
     */
    private static final Map<String, String> STATUS_DESCRIPTIONS = Map.of(
            "CREATED", "已创建物流单，等待商家发货",
            "PICKED_UP", "快递员已从商家仓库揽收",
            "SORTING", "包裹已到达中转分拣中心",
            "IN_TRANSIT", "包裹正在运输途中",
            "OUT_FOR_DELIVERY", "快递员正在派送至收货地址",
            "DELIVERED", "已送达"
    );

    /**
     * 每个状态对应的模拟地点
     */
    private static final Map<String, String> STATUS_LOCATIONS = Map.of(
            "CREATED", "商家仓库",
            "PICKED_UP", "发货城市集散中心",
            "SORTING", "中转分拣中心",
            "IN_TRANSIT", "运输途中",
            "OUT_FOR_DELIVERY", "收货城市配送站",
            "DELIVERED", "收货地址"
    );

    public LogisticsScheduler(LogisticsRecordMapper logisticsRecordMapper,
                              LogisticsTrackMapper logisticsTrackMapper,
                              OrderMapper orderMapper,
                              RefundMapper refundMapper,
                              ShipmentService shipmentService,
                              RefundService refundService,
                              ApplicationEventPublisher eventPublisher) {
        this.logisticsRecordMapper = logisticsRecordMapper;
        this.logisticsTrackMapper = logisticsTrackMapper;
        this.orderMapper = orderMapper;
        this.refundMapper = refundMapper;
        this.shipmentService = shipmentService;
        this.refundService = refundService;
        this.eventPublisher = eventPublisher;
    }

    @Scheduled(fixedDelay = 30000)
    @Transactional
    public void advanceLogisticsStatus() {
        // 查找所有未完成的物流记录
        LambdaQueryWrapper<LogisticsRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.notIn(LogisticsRecordDO::getStatus, "DELIVERED");
        List<LogisticsRecordDO> records = logisticsRecordMapper.selectList(wrapper);

        for (LogisticsRecordDO record : records) {
            try {
                String currentStatus = record.getStatus();
                if (currentStatus == null) {
                    log.warn("物流记录状态为空: recordId={}", record.getId());
                    continue;
                }

                // 检查是否超过推进间隔（每3小时推进一次）
                if (record.getUpdatedAt() != null) {
                    long secondsSinceUpdate = Duration.between(record.getUpdatedAt(), LocalDateTime.now()).getSeconds();
                    if (secondsSinceUpdate < logisticsIntervalSeconds) {
                        continue;
                    }
                }

                int currentIndex = STATUS_FLOW.indexOf(currentStatus);
                if (currentIndex < 0 || currentIndex >= STATUS_FLOW.size() - 1) {
                    continue;
                }

                // 推进到下一个状态
                String nextStatus = STATUS_FLOW.get(currentIndex + 1);
                record.setStatus(nextStatus);

                // 查询订单获取地址信息
                LambdaQueryWrapper<OrderDO> orderWrapper = new LambdaQueryWrapper<>();
                orderWrapper.eq(OrderDO::getOrderNo, record.getOrderNo());
                OrderDO order = orderMapper.selectOne(orderWrapper);
                String receiverAddress = order != null && order.getReceiverAddress() != null
                        ? order.getReceiverAddress() : "收货地址";
                String receiverName = order != null && order.getReceiverName() != null
                        ? order.getReceiverName() : "";

                // 如果到达DELIVERED状态，设置deliveredAt
                if ("DELIVERED".equals(nextStatus)) {
                    record.setDeliveredAt(LocalDateTime.now());
                    logisticsRecordMapper.updateById(record);

                    // 更新对应订单状态为DELIVERED
                    if (order != null) {
                        order.setStatus(ORDER_STATUS_DELIVERED);
                        order.setDeliveredAt(LocalDateTime.now());
                        orderMapper.updateById(order);

                        // 发布物流送达事件
                        eventPublisher.publishEvent(new LogisticsDeliveredEvent(
                                record.getOrderNo(), record.getOrderId()));
                    }
                } else {
                    logisticsRecordMapper.updateById(record);
                    // 同步更新订单状态
                    if ("IN_TRANSIT".equals(nextStatus) && order != null) {
                        order.setStatus("IN_TRANSIT");
                        orderMapper.updateById(order);
                    }
                }

                // 动态生成轨迹描述，包含地址信息
                String description = buildDescription(nextStatus, receiverAddress, receiverName);

                // 创建物流轨迹记录
                LogisticsTrackDO track = new LogisticsTrackDO();
                track.setLogisticsId(record.getId());
                track.setStatus(nextStatus);
                track.setDescription(description);
                track.setLocation(STATUS_LOCATIONS.get(nextStatus));
                track.setOccurredAt(LocalDateTime.now());
                logisticsTrackMapper.insert(track);

                log.info("物流状态推进: orderNo={}, {} -> {}, interval={}s",
                        record.getOrderNo(), currentStatus, nextStatus, logisticsIntervalSeconds);
            } catch (Exception e) {
                log.error("物流状态推进失败: orderNo={}, error={}", record.getOrderNo(), e.getMessage());
            }
        }
    }

    private String buildDescription(String status, String receiverAddress, String receiverName) {
        String base = STATUS_DESCRIPTIONS.getOrDefault(status, status);
        return switch (status) {
            case "PICKED_UP" -> base + "，即将发往 " + receiverAddress;
            case "SORTING" -> base + "，下一站：" + receiverAddress;
            case "IN_TRANSIT" -> base + "，预计送达：" + receiverAddress;
            case "OUT_FOR_DELIVERY" -> base + "（" + receiverAddress + "）";
            case "DELIVERED" -> base + "（" + receiverAddress + "），签收人：" + (receiverName.isEmpty() ? "本人" : receiverName);
            default -> base;
        };
    }

    /**
     * 自动发货：每30秒检查一次，对已支付超过1分钟的订单自动发货
     */
    @Scheduled(fixedDelay = 30000)
    public void autoShipPaidOrders() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(1);

        LambdaQueryWrapper<OrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDO::getStatus, "PAID");
        wrapper.isNotNull(OrderDO::getPaidAt);
        wrapper.lt(OrderDO::getPaidAt, threshold);
        List<OrderDO> orders = orderMapper.selectList(wrapper);

        for (OrderDO order : orders) {
            try {
                shipmentService.autoShipOrder(order.getOrderNo());
            } catch (Exception e) {
                log.error("自动发货失败: orderNo={}, error={}", order.getOrderNo(), e.getMessage());
            }
        }
    }

    /**
     * 自动退款审批：每30秒检查一次，对申请超过3分钟的退款自动同意
     */
    @Scheduled(fixedDelay = 30000)
    public void autoApproveRefunds() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(3);

        LambdaQueryWrapper<RefundDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RefundDO::getStatus, "PENDING");
        wrapper.lt(RefundDO::getCreatedAt, threshold);
        List<RefundDO> refunds = refundMapper.selectList(wrapper);

        for (RefundDO refund : refunds) {
            try {
                refundService.autoApproveRefund(refund.getOrderNo());
            } catch (Exception e) {
                log.error("自动退款审批失败: orderNo={}, error={}", refund.getOrderNo(), e.getMessage());
            }
        }
    }
}
