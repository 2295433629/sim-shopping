package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * 评价实体，对应 t_review 表，存储商品评价信息
 *
 * @author Sim Team
 * @since 1.0.0
 */
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
     * 获取Shop Id
     * @return 返回结果
     */
    public Long getShopId() { return this.shopId; }
    /**
     * set Shop Id
     * @param shopId shopId
     */
    public void setShopId(Long shopId) { this.shopId = shopId; }
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
     * 获取Order Id
     * @return 返回结果
     */
    public Long getOrderId() { return this.orderId; }
    /**
     * set Order Id
     * @param orderId orderId
     */
    public void setOrderId(Long orderId) { this.orderId = orderId; }
}
