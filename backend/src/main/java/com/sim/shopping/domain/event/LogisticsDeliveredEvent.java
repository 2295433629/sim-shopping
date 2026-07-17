package com.sim.shopping.domain.event;

/**
 * LogisticsDeliveredEvent
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class LogisticsDeliveredEvent {
    private final String orderNo;
    private final Long orderId;

    public LogisticsDeliveredEvent(String orderNo, Long orderId) {
        this.orderNo = orderNo;
        this.orderId = orderId;
    }

    /**
     * 获取Order No
     * @return 返回结果
     */
    public String getOrderNo() { return orderNo; }
    /**
     * 获取Order Id
     * @return 返回结果
     */
    public Long getOrderId() { return orderId; }
}
