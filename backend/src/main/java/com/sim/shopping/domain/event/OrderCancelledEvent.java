package com.sim.shopping.domain.event;

public class OrderCancelledEvent {
    private final String orderNo;
    private final Long userId;
    private final Long shopId;

    public OrderCancelledEvent(String orderNo, Long userId, Long shopId) {
        this.orderNo = orderNo;
        this.userId = userId;
        this.shopId = shopId;
    }

    public String getOrderNo() { return orderNo; }
    public Long getUserId() { return userId; }
    public Long getShopId() { return shopId; }
}
