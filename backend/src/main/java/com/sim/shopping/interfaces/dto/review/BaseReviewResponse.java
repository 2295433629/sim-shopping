package com.sim.shopping.interfaces.dto.review;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 评价响应公共基类，封装评价的核心字段，供ReviewResponse和MerchantReviewResponse复用
 *
 * @author Sim Team
 * @since 1.0.0
 */
public abstract class BaseReviewResponse {

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

    /** 获取Id */
    public Long getId() { return this.id; }
    /** set Id */
    public void setId(Long id) { this.id = id; }

    /** 获取Order No */
    public String getOrderNo() { return this.orderNo; }
    /** set Order No */
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }

    /** 获取Product Id */
    public Long getProductId() { return this.productId; }
    /** set Product Id */
    public void setProductId(Long productId) { this.productId = productId; }

    /** 获取Product Name */
    public String getProductName() { return this.productName; }
    /** set Product Name */
    public void setProductName(String productName) { this.productName = productName; }

    /** 获取User Id */
    public Long getUserId() { return this.userId; }
    /** set User Id */
    public void setUserId(Long userId) { this.userId = userId; }

    /** 获取Username */
    public String getUsername() { return this.username; }
    /** set Username */
    public void setUsername(String username) { this.username = username; }

    /** 获取Rating */
    public Integer getRating() { return this.rating; }
    /** set Rating */
    public void setRating(Integer rating) { this.rating = rating; }

    /** 获取Content */
    public String getContent() { return this.content; }
    /** set Content */
    public void setContent(String content) { this.content = content; }

    /** 获取Images */
    public List<String> getImages() { return this.images; }
    /** set Images */
    public void setImages(List<String> images) { this.images = images; }

    /** 获取Status */
    public String getStatus() { return this.status; }
    /** set Status */
    public void setStatus(String status) { this.status = status; }

    /** 获取Merchant Reply */
    public String getMerchantReply() { return this.merchantReply; }
    /** set Merchant Reply */
    public void setMerchantReply(String merchantReply) { this.merchantReply = merchantReply; }

    /** 获取Created At */
    public LocalDateTime getCreatedAt() { return this.createdAt; }
    /** set Created At */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
