package com.sim.shopping.interfaces.dto.refund;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RefundVO {
    private Long refundId;
    private String orderNo;
    private String refundType;
    private String status;
    private String reason;
    private BigDecimal amount;
    private String shopResponse;
    private LocalDateTime handledAt;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;

    public Long getRefundId() { return refundId; }
    public void setRefundId(Long refundId) { this.refundId = refundId; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public String getRefundType() { return refundType; }
    public void setRefundType(String refundType) { this.refundType = refundType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getShopResponse() { return shopResponse; }
    public void setShopResponse(String shopResponse) { this.shopResponse = shopResponse; }
    public LocalDateTime getHandledAt() { return handledAt; }
    public void setHandledAt(LocalDateTime handledAt) { this.handledAt = handledAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
