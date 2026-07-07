package com.sim.shopping.interfaces.dto.coupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class UserCouponResponse {

    private Long id;
    private Long userId;
    private Long couponId;
    private String couponCode;
    private String couponName;
    private String couponType;
    private BigDecimal discountValue;
    private BigDecimal minSpend;
    private LocalDateTime validStartTime;
    private LocalDateTime validEndTime;
    private String applicableScope;
    private String applicableIds;
    private LocalDateTime claimedAt;
    private LocalDateTime usedAt;
    private String orderNo;
    private String status;

    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return this.userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getCouponId() { return this.couponId; }
    public void setCouponId(Long couponId) { this.couponId = couponId; }

    public String getCouponCode() { return this.couponCode; }
    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }

    public String getCouponName() { return this.couponName; }
    public void setCouponName(String couponName) { this.couponName = couponName; }

    public String getCouponType() { return this.couponType; }
    public void setCouponType(String couponType) { this.couponType = couponType; }

    public BigDecimal getDiscountValue() { return this.discountValue; }
    public void setDiscountValue(BigDecimal discountValue) { this.discountValue = discountValue; }

    public BigDecimal getMinSpend() { return this.minSpend; }
    public void setMinSpend(BigDecimal minSpend) { this.minSpend = minSpend; }

    public LocalDateTime getValidStartTime() { return this.validStartTime; }
    public void setValidStartTime(LocalDateTime validStartTime) { this.validStartTime = validStartTime; }

    public LocalDateTime getValidEndTime() { return this.validEndTime; }
    public void setValidEndTime(LocalDateTime validEndTime) { this.validEndTime = validEndTime; }

    public String getApplicableScope() { return this.applicableScope; }
    public void setApplicableScope(String applicableScope) { this.applicableScope = applicableScope; }

    public String getApplicableIds() { return this.applicableIds; }
    public void setApplicableIds(String applicableIds) { this.applicableIds = applicableIds; }

    public LocalDateTime getClaimedAt() { return this.claimedAt; }
    public void setClaimedAt(LocalDateTime claimedAt) { this.claimedAt = claimedAt; }

    public LocalDateTime getUsedAt() { return this.usedAt; }
    public void setUsedAt(LocalDateTime usedAt) { this.usedAt = usedAt; }

    public String getOrderNo() { return this.orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }

    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }
}
