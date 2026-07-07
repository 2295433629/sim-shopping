package com.sim.shopping.interfaces.dto.order;

import java.util.List;

public class CreateOrderResponse {
    private List<OrderResponse> orders;

    public CreateOrderResponse(List<OrderResponse> orders) {
        this.orders = orders;
    }

    public List<OrderResponse> getOrders() { return orders; }
    public void setOrders(List<OrderResponse> orders) { this.orders = orders; }
}
