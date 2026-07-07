package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@TableName("t_review_image")
public class ReviewImageDO extends BaseEntity {
    private Integer sortOrder;
    private Long reviewId;
    private String imageUrl;

    public Integer getSortOrder() { return this.sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public Long getReviewId() { return this.reviewId; }
    public void setReviewId(Long reviewId) { this.reviewId = reviewId; }
    public String getImageUrl() { return this.imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
