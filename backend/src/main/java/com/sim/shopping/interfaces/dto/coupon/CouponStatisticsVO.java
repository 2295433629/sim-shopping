package com.sim.shopping.interfaces.dto.coupon;

public class CouponStatisticsVO {

    private Long totalCoupons;
    private Long totalClaimed;
    private Long totalUsed;
    private Long activeCoupons;
    private Long expiredCoupons;

    public Long getTotalCoupons() { return this.totalCoupons; }
    public void setTotalCoupons(Long totalCoupons) { this.totalCoupons = totalCoupons; }

    public Long getTotalClaimed() { return this.totalClaimed; }
    public void setTotalClaimed(Long totalClaimed) { this.totalClaimed = totalClaimed; }

    public Long getTotalUsed() { return this.totalUsed; }
    public void setTotalUsed(Long totalUsed) { this.totalUsed = totalUsed; }

    public Long getActiveCoupons() { return this.activeCoupons; }
    public void setActiveCoupons(Long activeCoupons) { this.activeCoupons = activeCoupons; }

    public Long getExpiredCoupons() { return this.expiredCoupons; }
    public void setExpiredCoupons(Long expiredCoupons) { this.expiredCoupons = expiredCoupons; }
}
