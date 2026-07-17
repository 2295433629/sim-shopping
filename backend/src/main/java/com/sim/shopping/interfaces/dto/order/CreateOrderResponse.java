package com.sim.shopping.interfaces.dto.order;

import java.util.List;

/**
 * 创建订单响应DTO，包含创建的订单列表
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class CreateOrderResponse {
    private List<OrderResponse> orders;

    public CreateOrderResponse(List<OrderResponse> orders) {
        this.orders = orders;
    }

    /**
     * 获取Orders
     * @return 返回结果
     */
    public List<OrderResponse> getOrders() { return orders; }
    /**
     * set Orders
     * @param orders orders
     */
    public void setOrders(List<OrderResponse> orders) { this.orders = orders; }
}
