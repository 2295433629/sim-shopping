package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("t_user_coupon")
public class UserCouponDO extends BaseEntity {

    private Long userId;
    private Long couponId;
    private LocalDateTime claimedAt;
    private LocalDateTime usedAt;
    private String orderNo;
    private String status;

    public Long getUserId() { return this.userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getCouponId() { return this.couponId; }
    public void setCouponId(Long couponId) { this.couponId = couponId; }

    public LocalDateTime getClaimedAt() { return this.claimedAt; }
    public void setClaimedAt(LocalDateTime claimedAt) { this.claimedAt = claimedAt; }

    public LocalDateTime getUsedAt() { return this.usedAt; }
    public void setUsedAt(LocalDateTime usedAt) { this.usedAt = usedAt; }

    public String getOrderNo() { return this.orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }

    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }
}
