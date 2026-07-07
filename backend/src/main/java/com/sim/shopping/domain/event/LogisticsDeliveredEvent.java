package com.sim.shopping.domain.event;

public class LogisticsDeliveredEvent {
    private final String orderNo;
    private final Long orderId;

    public LogisticsDeliveredEvent(String orderNo, Long orderId) {
        this.orderNo = orderNo;
        this.orderId = orderId;
    }

    public String getOrderNo() { return orderNo; }
    public Long getOrderId() { return orderId; }
}
