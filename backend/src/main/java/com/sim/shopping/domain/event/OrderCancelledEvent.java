package com.sim.shopping.domain.event;

/**
 * OrderCancelledEvent
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class OrderCancelledEvent {
    private final String orderNo;
    private final Long userId;
    private final Long shopId;

    public OrderCancelledEvent(String orderNo, Long userId, Long shopId) {
        this.orderNo = orderNo;
        this.userId = userId;
        this.shopId = shopId;
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
}
