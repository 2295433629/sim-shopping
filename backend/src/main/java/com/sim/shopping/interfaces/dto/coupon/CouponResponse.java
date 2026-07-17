package com.sim.shopping.interfaces.dto.coupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Coupon响应对象，封装接口出参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class CouponResponse {

    private Long id;
    private String couponCode;
    private String couponName;
    private String couponType;
    private BigDecimal discountValue;
    private BigDecimal minSpend;
    private Integer totalQuantity;
    private Integer claimedQuantity;
    private Integer usedQuantity;
    private Integer remainingQuantity;
    private LocalDateTime validStartTime;
    private LocalDateTime validEndTime;
    private String applicableScope;
    private String applicableIds;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** 获取Id */
    public Long getId() { return this.id; }
    /** set Id */
    public void setId(Long id) { this.id = id; }

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
     * 获取Total Quantity
     * @return 返回结果
     */
    public Integer getTotalQuantity() { return this.totalQuantity; }
    /**
     * set Total Quantity
     * @param totalQuantity totalQuantity
     */
    public void setTotalQuantity(Integer totalQuantity) { this.totalQuantity = totalQuantity; }

    /**
     * 获取Claimed Quantity
     * @return 返回结果
     */
    public Integer getClaimedQuantity() { return this.claimedQuantity; }
    /**
     * set Claimed Quantity
     * @param claimedQuantity claimedQuantity
     */
    public void setClaimedQuantity(Integer claimedQuantity) { this.claimedQuantity = claimedQuantity; }

    /**
     * 获取Used Quantity
     * @return 返回结果
     */
    public Integer getUsedQuantity() { return this.usedQuantity; }
    /**
     * set Used Quantity
     * @param usedQuantity usedQuantity
     */
    public void setUsedQuantity(Integer usedQuantity) { this.usedQuantity = usedQuantity; }

    /**
     * 获取Remaining Quantity
     * @return 返回结果
     */
    public Integer getRemainingQuantity() { return this.remainingQuantity; }
    /**
     * set Remaining Quantity
     * @param remainingQuantity remainingQuantity
     */
    public void setRemainingQuantity(Integer remainingQuantity) { this.remainingQuantity = remainingQuantity; }

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
     * 获取Status
     * @return 返回结果
     */
    public String getStatus() { return this.status; }
    /**
     * set Status
     * @param status status
     */
    public void setStatus(String status) { this.status = status; }

    /** 获取Created At */
    public LocalDateTime getCreatedAt() { return this.createdAt; }
    /** set Created At */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    /** 获取Updated At */
    public LocalDateTime getUpdatedAt() { return this.updatedAt; }
    /** set Updated At */
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
