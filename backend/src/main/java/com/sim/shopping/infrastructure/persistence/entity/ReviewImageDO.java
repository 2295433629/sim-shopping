package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * ReviewImage数据实体，对应数据库表
 *
 * @author Sim Team
 * @since 1.0.0
 */
@TableName("t_review_image")
public class ReviewImageDO extends BaseEntity {
    private Integer sortOrder;
    private Long reviewId;
    private String imageUrl;

    /**
     * 获取Sort Order
     * @return 返回结果
     */
    public Integer getSortOrder() { return this.sortOrder; }
    /**
     * set Sort Order
     * @param sortOrder sortOrder
     */
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    /**
     * 获取Review Id
     * @return 返回结果
     */
    public Long getReviewId() { return this.reviewId; }
    /**
     * set Review Id
     * @param reviewId reviewId
     */
    public void setReviewId(Long reviewId) { this.reviewId = reviewId; }
    /**
     * 获取Image Url
     * @return 返回结果
     */
    public String getImageUrl() { return this.imageUrl; }
    /**
     * set Image Url
     * @param imageUrl imageUrl
     */
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
