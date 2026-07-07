package com.sim.shopping.domain.event;

import java.math.BigDecimal;

public class OrderCreatedEvent {
    private final String orderNo;
    private final Long userId;
    private final Long shopId;
    private final BigDecimal totalAmount;

    public OrderCreatedEvent(String orderNo, Long userId, Long shopId, BigDecimal totalAmount) {
        this.orderNo = orderNo;
        this.userId = userId;
        this.shopId = shopId;
        this.totalAmount = totalAmount;
    }

    public String getOrderNo() { return orderNo; }
    public Long getUserId() { return userId; }
    public Long getShopId() { return shopId; }
    public BigDecimal getTotalAmount() { return totalAmount; }
}
