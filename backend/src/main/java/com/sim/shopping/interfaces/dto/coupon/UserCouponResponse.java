package com.sim.shopping.interfaces.dto.coupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * UserCoupon响应对象，封装接口出参
 *
 * @author Sim Team
 * @since 1.0.0
 */
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

    /** 获取Id */
    public Long getId() { return this.id; }
    /** set Id */
    public void setId(Long id) { this.id = id; }

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
     * 获取Coupon Id
     * @return 返回结果
     */
    public Long getCouponId() { return this.couponId; }
    /**
     * set Coupon Id
     * @param couponId couponId
     */
    public void setCouponId(Long couponId) { this.couponId = couponId; }

    /**
     * 获取Coupon Code
     * @return 返回结果
     */
    public String getCouponCode() { return this.couponCode; }
    /**
     * set Coupon Code
     * @param couponCode couponCode
     */
    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }

    /**
     * 获取Coupon Name
     * @return 返回结果
     */
    public String getCouponName() { return this.couponName; }
    /**
     * set Coupon Name
     * @param couponName couponName
     */
    public void setCouponName(String couponName) { this.couponName = couponName; }

    /**
     * 获取Coupon Type
     * @return 返回结果
     */
    public String getCouponType() { return this.couponType; }
    /**
     * set Coupon Type
     * @param couponType couponType
     */
    public void setCouponType(String couponType) { this.couponType = couponType; }

    /**
     * 获取Discount Value
     * @return 返回结果
     */
    public BigDecimal getDiscountValue() { return this.discountValue; }
    /**
     * set Discount Value
     * @param discountValue discountValue
     */
    public void setDiscountValue(BigDecimal discountValue) { this.discountValue = discountValue; }

    /**
     * 获取Min Spend
     * @return 返回结果
     */
    public BigDecimal getMinSpend() { return this.minSpend; }
    /**
     * set Min Spend
     * @param minSpend minSpend
     */
    public void setMinSpend(BigDecimal minSpend) { this.minSpend = minSpend; }

    /**
     * 获取Valid Start Time
     * @return 返回结果
     */
    public LocalDateTime getValidStartTime() { return this.validStartTime; }
    /**
     * set Valid Start Time
     * @param validStartTime validStartTime
     */
    public void setValidStartTime(LocalDateTime validStartTime) { this.validStartTime = validStartTime; }

    /**
     * 获取Valid End Time
     * @return 返回结果
     */
    public LocalDateTime getValidEndTime() { return this.validEndTime; }
    /**
     * set Valid End Time
     * @param validEndTime validEndTime
     */
    public void setValidEndTime(LocalDateTime validEndTime) { this.validEndTime = validEndTime; }

    /**
     * 获取Applicable Scope
     * @return 返回结果
     */
    public String getApplicableScope() { return this.applicableScope; }
    /**
     * set Applicable Scope
     * @param applicableScope applicableScope
     */
    public void setApplicableScope(String applicableScope) { this.applicableScope = applicableScope; }

    /**
     * 获取Applicable Ids
     * @return 返回结果
     */
    public String getApplicableIds() { return this.applicableIds; }
    /**
     * set Applicable Ids
     * @param applicableIds applicableIds
     */
    public void setApplicableIds(String applicableIds) { this.applicableIds = applicableIds; }

    /**
     * 获取Claimed At
     * @return 返回结果
     */
    public LocalDateTime getClaimedAt() { return this.claimedAt; }
    /**
     * set Claimed At
     * @param claimedAt claimedAt
     */
    public void setClaimedAt(LocalDateTime claimedAt) { this.claimedAt = claimedAt; }

    /**
     * 获取Used At
     * @return 返回结果
     */
    public LocalDateTime getUsedAt() { return this.usedAt; }
    /**
     * set Used At
     * @param usedAt usedAt
     */
    public void setUsedAt(LocalDateTime usedAt) { this.usedAt = usedAt; }

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
     * 获取Status
     * @return 返回结果
     */
    public String getStatus() { return this.status; }
    /**
     * set Status
     * @param status status
     */
    public void setStatus(String status) { this.status = status; }
}
