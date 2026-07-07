package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@TableName("t_review")
public class ReviewDO extends BaseEntity {
    private LocalDateTime merchantRepliedAt;
    private String orderNo;
    private Long shopId;
    private Integer rating;
    private Long productId;
    private String status;
    private String content;
    private Long userId;
    private String merchantReply;
    private Long orderId;

    public LocalDateTime getMerchantRepliedAt() { return this.merchantRepliedAt; }
    public void setMerchantRepliedAt(LocalDateTime merchantRepliedAt) { this.merchantRepliedAt = merchantRepliedAt; }
    public String getOrderNo() { return this.orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public Long getShopId() { return this.shopId; }
    public void setShopId(Long shopId) { this.shopId = shopId; }
    public Integer getRating() { return this.rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public Long getProductId() { return this.productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }
    public String getContent() { return this.content; }
    public void setContent(String content) { this.content = content; }
    public Long getUserId() { return this.userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getMerchantReply() { return this.merchantReply; }
    public void setMerchantReply(String merchantReply) { this.merchantReply = merchantReply; }
    public Long getOrderId() { return this.orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
}
