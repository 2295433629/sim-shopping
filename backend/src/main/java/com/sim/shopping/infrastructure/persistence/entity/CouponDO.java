package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("t_coupon")
public class CouponDO extends BaseEntity {

    private String couponCode;
    private String couponName;
    private String couponType;
    private BigDecimal discountValue;
    private BigDecimal minSpend;
    private Integer totalQuantity;
    private Integer claimedQuantity;
    private Integer usedQuantity;
    private LocalDateTime validStartTime;
    private LocalDateTime validEndTime;
    private String applicableScope;
    private String applicableIds;
    private String status;

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

    public Integer getTotalQuantity() { return this.totalQuantity; }
    public void setTotalQuantity(Integer totalQuantity) { this.totalQuantity = totalQuantity; }

    public Integer getClaimedQuantity() { return this.claimedQuantity; }
    public void setClaimedQuantity(Integer claimedQuantity) { this.claimedQuantity = claimedQuantity; }

    public Integer getUsedQuantity() { return this.usedQuantity; }
    public void setUsedQuantity(Integer usedQuantity) { this.usedQuantity = usedQuantity; }

    public LocalDateTime getValidStartTime() { return this.validStartTime; }
    public void setValidStartTime(LocalDateTime validStartTime) { this.validStartTime = validStartTime; }

    public LocalDateTime getValidEndTime() { return this.validEndTime; }
    public void setValidEndTime(LocalDateTime validEndTime) { this.validEndTime = validEndTime; }

    public String getApplicableScope() { return this.applicableScope; }
    public void setApplicableScope(String applicableScope) { this.applicableScope = applicableScope; }

    public String getApplicableIds() { return this.applicableIds; }
    public void setApplicableIds(String applicableIds) { this.applicableIds = applicableIds; }

    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }
}
