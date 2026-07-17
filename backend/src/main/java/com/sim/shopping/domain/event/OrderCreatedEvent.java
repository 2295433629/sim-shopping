package com.sim.shopping.domain.event;

import java.math.BigDecimal;

/**
 * OrderCreatedEvent
 *
 * @author Sim Team
 * @since 1.0.0
 */
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

    /**
     * 获取Order No
     * @return 返回结果
     */
    public String getOrderNo() { return orderNo; }
    /**
     * 获取User Id
     * @return 返回结果
     */
    public Long getUserId() { return userId; }
    /**
     * 获取Shop Id
     * @return 返回结果
     */
    public Long getShopId() { return shopId; }
    /**
     * 获取Total Amount
     * @return 返回结果
     */
    public BigDecimal getTotalAmount() { return totalAmount; }
}
