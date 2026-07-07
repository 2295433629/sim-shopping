package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@TableName("t_payment")
public class PaymentDO extends BaseEntity {
    private Long orderId;
    private LocalDateTime paidAt;
    private String orderNo;
    private String paymentNo;
    private String paymentMethod;
    private BigDecimal amount;
    private String status;

    public Long getOrderId() { return this.orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public LocalDateTime getPaidAt() { return this.paidAt; }
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }
    public String getOrderNo() { return this.orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public String getPaymentNo() { return this.paymentNo; }
    public void setPaymentNo(String paymentNo) { this.paymentNo = paymentNo; }
    public String getPaymentMethod() { return this.paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public BigDecimal getAmount() { return this.amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }
}
