package com.sim.shopping.interfaces.dto.product;

import java.math.BigDecimal;

public class ReviewSummaryVO {
    private BigDecimal avgRating;
    private Integer reviewCount;
    private BigDecimal goodRate;

    public BigDecimal getAvgRating() { return this.avgRating; }
    public void setAvgRating(BigDecimal avgRating) { this.avgRating = avgRating; }
    public Integer getReviewCount() { return this.reviewCount; }
    public void setReviewCount(Integer reviewCount) { this.reviewCount = reviewCount; }
    public BigDecimal getGoodRate() { return this.goodRate; }
    public void setGoodRate(BigDecimal goodRate) { this.goodRate = goodRate; }
}
