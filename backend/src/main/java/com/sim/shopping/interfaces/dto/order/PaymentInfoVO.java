package com.sim.shopping.interfaces.dto.order;

public class PaymentInfoVO {
    private String paymentNo;
    private String paymentMethod;
    private java.math.BigDecimal amount;
    private String status;
    private java.time.LocalDateTime paidAt;

    public String getPaymentNo() { return paymentNo; }
    public void setPaymentNo(String paymentNo) { this.paymentNo = paymentNo; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public java.math.BigDecimal getAmount() { return amount; }
    public void setAmount(java.math.BigDecimal amount) { this.amount = amount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public java.time.LocalDateTime getPaidAt() { return paidAt; }
    public void setPaidAt(java.time.LocalDateTime paidAt) { this.paidAt = paidAt; }
}
