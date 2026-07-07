package com.sim.shopping.application.shipment;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sim.shopping.domain.event.LogisticsDeliveredEvent;
import com.sim.shopping.infrastructure.persistence.entity.LogisticsRecordDO;
import com.sim.shopping.infrastructure.persistence.entity.LogisticsTrackDO;
import com.sim.shopping.infrastructure.persistence.entity.OrderDO;
import com.sim.shopping.infrastructure.persistence.mapper.LogisticsRecordMapper;
import com.sim.shopping.infrastructure.persistence.mapper.LogisticsTrackMapper;
import com.sim.shopping.infrastructure.persistence.mapper.OrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    private final ApplicationEventPublisher eventPublisher;

    private static final String ORDER_STATUS_DELIVERED = "DELIVERED";

    /**
     * 物流状态推进顺序: CREATED -> PICKED_UP -> SORTING -> IN_TRANSIT -> OUT_FOR_DELIVERY -> DELIVERED
     */
    private static final List<String> STATUS_FLOW = Arrays.asList(
            "CREATED", "PICKED_UP", "SORTING", "IN_TRANSIT", "OUT_FOR_DELIVERY", "DELIVERED"
    );

    /**
     * 每个状态对应的轨迹描述
     */
    private static final Map<String, String> STATUS_DESCRIPTIONS = Map.of(
            "CREATED", "已创建物流单",
            "PICKED_UP", "快递员已揽收",
            "SORTING", "包裹已在分拣中心分拣",
            "IN_TRANSIT", "包裹运输中",
            "OUT_FOR_DELIVERY", "快递员派送中",
            "DELIVERED", "已签收"
    );

    /**
     * 每个状态对应的模拟地点
     */
    private static final Map<String, String> STATUS_LOCATIONS = Map.of(
            "CREATED", "",
            "PICKED_UP", "发货城市集散中心",
            "SORTING", "中转分拣中心",
            "IN_TRANSIT", "运输途中",
            "OUT_FOR_DELIVERY", "收货城市配送站",
            "DELIVERED", "收货地址"
    );

    public LogisticsScheduler(LogisticsRecordMapper logisticsRecordMapper,
                              LogisticsTrackMapper logisticsTrackMapper,
                              OrderMapper orderMapper,
                              ApplicationEventPublisher eventPublisher) {
        this.logisticsRecordMapper = logisticsRecordMapper;
        this.logisticsTrackMapper = logisticsTrackMapper;
        this.orderMapper = orderMapper;
        this.eventPublisher = eventPublisher;
    }

    @Scheduled(fixedDelay = 30000)
    public void advanceLogisticsStatus() {
        // 查找所有未完成的物流记录（CREATED/PICKED_UP/SORTING/IN_TRANSIT/OUT_FOR_DELIVERY）
        LambdaQueryWrapper<LogisticsRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.notIn(LogisticsRecordDO::getStatus, "DELIVERED");
        List<LogisticsRecordDO> records = logisticsRecordMapper.selectList(wrapper);

        for (LogisticsRecordDO record : records) {
            try {
                String currentStatus = record.getStatus();
                int currentIndex = STATUS_FLOW.indexOf(currentStatus);
                if (currentIndex < 0 || currentIndex >= STATUS_FLOW.size() - 1) {
                    continue;
                }

                // 推进到下一个状态
                String nextStatus = STATUS_FLOW.get(currentIndex + 1);
                record.setStatus(nextStatus);

                // 如果到达DELIVERED状态，设置deliveredAt
                if ("DELIVERED".equals(nextStatus)) {
                    record.setDeliveredAt(LocalDateTime.now());
                    logisticsRecordMapper.updateById(record);

                    // 更新对应订单状态为DELIVERED
                    LambdaQueryWrapper<OrderDO> orderWrapper = new LambdaQueryWrapper<>();
                    orderWrapper.eq(OrderDO::getOrderNo, record.getOrderNo());
                    OrderDO order = orderMapper.selectOne(orderWrapper);
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
                    if ("IN_TRANSIT".equals(nextStatus)) {
                        LambdaQueryWrapper<OrderDO> orderWrapper = new LambdaQueryWrapper<>();
                        orderWrapper.eq(OrderDO::getOrderNo, record.getOrderNo());
                        OrderDO order = orderMapper.selectOne(orderWrapper);
                        if (order != null) {
                            order.setStatus("IN_TRANSIT");
                            orderMapper.updateById(order);
                        }
                    }
                }

                // 创建物流轨迹记录
                LogisticsTrackDO track = new LogisticsTrackDO();
                track.setLogisticsId(record.getId());
                track.setStatus(nextStatus);
                track.setDescription(STATUS_DESCRIPTIONS.getOrDefault(nextStatus, nextStatus));
                track.setLocation(STATUS_LOCATIONS.get(nextStatus));
                track.setOccurredAt(LocalDateTime.now());
                logisticsTrackMapper.insert(track);

                log.info("物流状态推进: orderNo={}, {} -> {}", record.getOrderNo(), currentStatus, nextStatus);
            } catch (Exception e) {
                log.error("物流状态推进失败: orderNo={}, error={}", record.getOrderNo(), e.getMessage());
            }
        }
    }
}
