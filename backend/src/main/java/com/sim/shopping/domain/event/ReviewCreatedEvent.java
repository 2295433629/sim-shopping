package com.sim.shopping.domain.event;

public class ReviewCreatedEvent {
    private final String orderNo;
    private final Long productId;
    private final Integer rating;

    public ReviewCreatedEvent(String orderNo, Long productId, Integer rating) {
        this.orderNo = orderNo;
        this.productId = productId;
        this.rating = rating;
    }

    public String getOrderNo() { return orderNo; }
    public Long getProductId() { return productId; }
    public Integer getRating() { return rating; }
}
