package com.sim.shopping.interfaces.dto.review;

import java.time.LocalDateTime;
import java.util.List;

public class MerchantReviewResponse {
    private Long id;
    private String orderNo;
    private Long productId;
    private String productName;
    private Long userId;
    private String username;
    private Integer rating;
    private String content;
    private List<String> images;
    private String status;
    private String merchantReply;
    private LocalDateTime createdAt;

    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }
    public String getOrderNo() { return this.orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public Long getProductId() { return this.productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductName() { return this.productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public Long getUserId() { return this.userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return this.username; }
    public void setUsername(String username) { this.username = username; }
    public Integer getRating() { return this.rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public String getContent() { return this.content; }
    public void setContent(String content) { this.content = content; }
    public List<String> getImages() { return this.images; }
    public void setImages(List<String> images) { this.images = images; }
    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }
    public String getMerchantReply() { return this.merchantReply; }
    public void setMerchantReply(String merchantReply) { this.merchantReply = merchantReply; }
    public LocalDateTime getCreatedAt() { return this.createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
