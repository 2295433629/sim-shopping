package com.sim.shopping.interfaces.dto.review;

import java.util.List;

public class CreateReviewRequest {
    private Long productId;
    private Long orderId;
    private String orderNo;
    private Integer rating;
    private String content;
    private List<String> imageUrls;

    public Long getProductId() { return this.productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Long getOrderId() { return this.orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public String getOrderNo() { return this.orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public Integer getRating() { return this.rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public String getContent() { return this.content; }
    public void setContent(String content) { this.content = content; }
    public List<String> getImageUrls() { return this.imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }
}
