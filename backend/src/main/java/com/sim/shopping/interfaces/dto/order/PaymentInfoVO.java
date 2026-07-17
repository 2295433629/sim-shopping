package com.sim.shopping.interfaces.dto.order;

/**
 * PaymentInfo视图对象，用于前端展示
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class PaymentInfoVO {
    private String paymentNo;
    private String paymentMethod;
    private java.math.BigDecimal amount;
    private String status;
    private java.time.LocalDateTime paidAt;

    /**
     * 获取Payment No
     * @return 返回结果
     */
    public String getPaymentNo() { return paymentNo; }
    /**
     * set Payment No
     * @param paymentNo paymentNo
     */
    public void setPaymentNo(String paymentNo) { this.paymentNo = paymentNo; }
    /**
     * 获取Payment Method
     * @return 返回结果
     */
    public String getPaymentMethod() { return paymentMethod; }
    /**
     * set Payment Method
     * @param paymentMethod paymentMethod
     */
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    /**
     * 获取Amount
     * @return 返回结果
     */
    public java.math.BigDecimal getAmount() { return amount; }
    /**
     * set Amount
     * @param amount amount
     */
    public void setAmount(java.math.BigDecimal amount) { this.amount = amount; }
    /**
     * 获取Status
     * @return 返回结果
     */
    public String getStatus() { return status; }
    /**
     * set Status
     * @param status status
     */
    public void setStatus(String status) { this.status = status; }
    /**
     * 获取Paid At
     * @return 返回结果
     */
    public java.time.LocalDateTime getPaidAt() { return paidAt; }
    /**
     * set Paid At
     * @param paidAt paidAt
     */
    public void setPaidAt(java.time.LocalDateTime paidAt) { this.paidAt = paidAt; }
}
