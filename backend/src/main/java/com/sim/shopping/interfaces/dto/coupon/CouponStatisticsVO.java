package com.sim.shopping.interfaces.dto.coupon;

/**
 * CouponStatistics视图对象，用于前端展示
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class CouponStatisticsVO {

    private Long totalCoupons;
    private Long totalClaimed;
    private Long totalUsed;
    private Long activeCoupons;
    private Long expiredCoupons;

    /**
     * 获取Total Coupons
     * @return 返回结果
     */
    public Long getTotalCoupons() { return this.totalCoupons; }
    /**
     * set Total Coupons
     * @param totalCoupons totalCoupons
     */
    public void setTotalCoupons(Long totalCoupons) { this.totalCoupons = totalCoupons; }

    /**
     * 获取Total Claimed
     * @return 返回结果
     */
    public Long getTotalClaimed() { return this.totalClaimed; }
    /**
     * set Total Claimed
     * @param totalClaimed totalClaimed
     */
    public void setTotalClaimed(Long totalClaimed) { this.totalClaimed = totalClaimed; }

    /**
     * 获取Total Used
     * @return 返回结果
     */
    public Long getTotalUsed() { return this.totalUsed; }
    /**
     * set Total Used
     * @param totalUsed totalUsed
     */
    public void setTotalUsed(Long totalUsed) { this.totalUsed = totalUsed; }

    /**
     * 获取Active Coupons
     * @return 返回结果
     */
    public Long getActiveCoupons() { return this.activeCoupons; }
    /**
     * set Active Coupons
     * @param activeCoupons activeCoupons
     */
    public void setActiveCoupons(Long activeCoupons) { this.activeCoupons = activeCoupons; }

    /**
     * 获取Expired Coupons
     * @return 返回结果
     */
    public Long getExpiredCoupons() { return this.expiredCoupons; }
    /**
     * set Expired Coupons
     * @param expiredCoupons expiredCoupons
     */
    public void setExpiredCoupons(Long expiredCoupons) { this.expiredCoupons = expiredCoupons; }
}
