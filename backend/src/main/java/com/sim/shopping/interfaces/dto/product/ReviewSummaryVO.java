package com.sim.shopping.interfaces.dto.product;

import java.math.BigDecimal;

/**
 * ReviewSummary视图对象，用于前端展示
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class ReviewSummaryVO {
    private BigDecimal avgRating;
    private Integer reviewCount;
    private BigDecimal goodRate;

    /**
     * 获取Avg Rating
     * @return 返回结果
     */
    public BigDecimal getAvgRating() { return this.avgRating; }
    /**
     * set Avg Rating
     * @param avgRating avgRating
     */
    public void setAvgRating(BigDecimal avgRating) { this.avgRating = avgRating; }
    /**
     * 获取Review Count
     * @return 返回结果
     */
    public Integer getReviewCount() { return this.reviewCount; }
    /**
     * set Review Count
     * @param reviewCount reviewCount
     */
    public void setReviewCount(Integer reviewCount) { this.reviewCount = reviewCount; }
    /**
     * 获取Good Rate
     * @return 返回结果
     */
    public BigDecimal getGoodRate() { return this.goodRate; }
    /**
     * set Good Rate
     * @param goodRate goodRate
     */
    public void setGoodRate(BigDecimal goodRate) { this.goodRate = goodRate; }
}
