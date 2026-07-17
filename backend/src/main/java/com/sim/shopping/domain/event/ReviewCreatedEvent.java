package com.sim.shopping.domain.event;

/**
 * ReviewCreatedEvent
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class ReviewCreatedEvent {
    private final String orderNo;
    private final Long productId;
    private final Integer rating;

    public ReviewCreatedEvent(String orderNo, Long productId, Integer rating) {
        this.orderNo = orderNo;
        this.productId = productId;
        this.rating = rating;
    }

    /**
     * 获取Order No
     * @return 返回结果
     */
    public String getOrderNo() { return orderNo; }
    /**
     * 获取Product Id
     * @return 返回结果
     */
    public Long getProductId() { return productId; }
    /**
     * 获取Rating
     * @return 返回结果
     */
    public Integer getRating() { return rating; }
}
