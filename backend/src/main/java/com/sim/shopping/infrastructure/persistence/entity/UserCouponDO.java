package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * 用户优惠券实体，对应 t_user_coupon 表，存储用户领取的优惠券
 *
 * @author Sim Team
 * @since 1.0.0
 */
@TableName("t_user_coupon")
public class UserCouponDO extends BaseEntity {

    private Long userId;
    private Long couponId;
    private LocalDateTime claimedAt;
    private LocalDateTime usedAt;
    private String orderNo;
    private String status;

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
