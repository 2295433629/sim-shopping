package com.sim.shopping.interfaces.dto.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentResponse {
    private String paymentNo;
    private String orderNo;
    private BigDecimal amount;
    private String paymentMethod;
    private String status;
    private LocalDateTime paidAt;

    public String getPaymentNo() { return this.paymentNo; }
    public void setPaymentNo(String paymentNo) { this.paymentNo = paymentNo; }
    public String getOrderNo() { return this.orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public BigDecimal getAmount() { return this.amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getPaymentMethod() { return this.paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getPaidAt() { return this.paidAt; }
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }
}
