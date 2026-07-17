package com.sim.shopping.interfaces.dto.review;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Review响应对象，封装接口出参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class ReviewResponse {
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
    private LocalDateTime merchantRepliedAt;
    private LocalDateTime createdAt;

    /** 获取Id */
    public Long getId() { return this.id; }
    /** set Id */
    public void setId(Long id) { this.id = id; }
    /**
     * 获取Order No
     * @return 返回结果
     */
    public String getOrderNo() { return this.orderNo; }
    /**
     * set Order No
     * @param orderNo orderNo
     */
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    /**
     * 获取Product Id
     * @return 返回结果
     */
    public Long getProductId() { return this.productId; }
    /**
     * set Product Id
     * @param productId productId
     */
    public void setProductId(Long productId) { this.productId = productId; }
    /**
     * 获取Product Name
     * @return 返回结果
     */
    public String getProductName() { return this.productName; }
    /**
     * set Product Name
     * @param productName productName
     */
    public void setProductName(String productName) { this.productName = productName; }
    /**
     * 获取User Id
     * @return 返回结果
     */
    public Long getUserId() { return this.userId; }
    /**
     * set User Id
     * @param userId userId
     */
    public void setUserId(Long userId) { this.userId = userId; }
    /**
     * 获取Username
     * @return 返回结果
     */
    public String getUsername() { return this.username; }
    /**
     * set Username
     * @param username username
     */
    public void setUsername(String username) { this.username = username; }
    /**
     * 获取Rating
     * @return 返回结果
     */
    public Integer getRating() { return this.rating; }
    /**
     * set Rating
     * @param rating rating
     */
    public void setRating(Integer rating) { this.rating = rating; }
    /**
     * 获取Content
     * @return 返回结果
     */
    public String getContent() { return this.content; }
    /**
     * set Content
     * @param content content
     */
    public void setContent(String content) { this.content = content; }
    /**
     * 获取Images
     * @return 返回结果
     */
    public List<String> getImages() { return this.images; }
    /**
     * set Images
     * @param images images
     */
    public void setImages(List<String> images) { this.images = images; }
    /**
     * 获取Status
     * @return 返回结果
     */
    public String getStatus() { return this.status; }
    /**
     * set Status
     * @param status status
     */
    public void setStatus(String status) { this.status = status; }
    /**
     * 获取Merchant Reply
     * @return 返回结果
     */
    public String getMerchantReply() { return this.merchantReply; }
    /**
     * set Merchant Reply
     * @param merchantReply merchantReply
     */
    public void setMerchantReply(String merchantReply) { this.merchantReply = merchantReply; }
    /**
     * 获取Merchant Replied At
     * @return 返回结果
     */
    public LocalDateTime getMerchantRepliedAt() { return this.merchantRepliedAt; }
    /**
     * set Merchant Replied At
     * @param merchantRepliedAt merchantRepliedAt
     */
    public void setMerchantRepliedAt(LocalDateTime merchantRepliedAt) { this.merchantRepliedAt = merchantRepliedAt; }
    /** 获取Created At */
    public LocalDateTime getCreatedAt() { return this.createdAt; }
    /** set Created At */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
