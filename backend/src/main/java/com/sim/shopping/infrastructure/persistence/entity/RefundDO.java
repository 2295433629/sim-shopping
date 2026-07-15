package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("t_order_refund")
public class RefundDO extends BaseEntity {
    private Long orderId;
    private String orderNo;
    private Long userId;
    private String refundType;
    private String status;
    private String reason;
    private BigDecimal amount;
    private String shopResponse;
    private LocalDateTime handledAt;
    private LocalDateTime completedAt;

    public Long getOrderId() { return this.orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public String getOrderNo() { return this.orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public Long getUserId() { return this.userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getRefundType() { return this.refundType; }
    public void setRefundType(String refundType) { this.refundType = refundType; }
    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }
    public String getReason() { return this.reason; }
    public void setReason(String reason) { this.reason = reason; }
    public BigDecimal getAmount() { return this.amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getShopResponse() { return this.shopResponse; }
    public void setShopResponse(String shopResponse) { this.shopResponse = shopResponse; }
    public LocalDateTime getHandledAt() { return this.handledAt; }
    public void setHandledAt(LocalDateTime handledAt) { this.handledAt = handledAt; }
    public LocalDateTime getCompletedAt() { return this.completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}
