package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@TableName("t_order")
public class OrderDO extends BaseEntity {
    private Long userId;
    private LocalDateTime paidAt;
    private LocalDateTime shippedAt;
    private LocalDateTime completedAt;
    private BigDecimal payAmount;
    private String remark;
    private Integer autoConfirm;
    private BigDecimal discountAmount;
    private String status;
    private String receiverAddress;
    private LocalDateTime cancelledAt;
    private String orderNo;
    private String receiverName;
    private Long shopId;
    private BigDecimal shippingFee;
    private BigDecimal totalAmount;
    private LocalDateTime deliveredAt;
    private String receiverPhone;

    public Long getUserId() { return this.userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public LocalDateTime getPaidAt() { return this.paidAt; }
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }
    public LocalDateTime getShippedAt() { return this.shippedAt; }
    public void setShippedAt(LocalDateTime shippedAt) { this.shippedAt = shippedAt; }
    public LocalDateTime getCompletedAt() { return this.completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    public BigDecimal getPayAmount() { return this.payAmount; }
    public void setPayAmount(BigDecimal payAmount) { this.payAmount = payAmount; }
    public String getRemark() { return this.remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Integer getAutoConfirm() { return this.autoConfirm; }
    public void setAutoConfirm(Integer autoConfirm) { this.autoConfirm = autoConfirm; }
    public BigDecimal getDiscountAmount() { return this.discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }
    public String getReceiverAddress() { return this.receiverAddress; }
    public void setReceiverAddress(String receiverAddress) { this.receiverAddress = receiverAddress; }
    public LocalDateTime getCancelledAt() { return this.cancelledAt; }
    public void setCancelledAt(LocalDateTime cancelledAt) { this.cancelledAt = cancelledAt; }
    public String getOrderNo() { return this.orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public String getReceiverName() { return this.receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }
    public Long getShopId() { return this.shopId; }
    public void setShopId(Long shopId) { this.shopId = shopId; }
    public BigDecimal getShippingFee() { return this.shippingFee; }
    public void setShippingFee(BigDecimal shippingFee) { this.shippingFee = shippingFee; }
    public BigDecimal getTotalAmount() { return this.totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public LocalDateTime getDeliveredAt() { return this.deliveredAt; }
    public void setDeliveredAt(LocalDateTime deliveredAt) { this.deliveredAt = deliveredAt; }
    public String getReceiverPhone() { return this.receiverPhone; }
    public void setReceiverPhone(String receiverPhone) { this.receiverPhone = receiverPhone; }
}
