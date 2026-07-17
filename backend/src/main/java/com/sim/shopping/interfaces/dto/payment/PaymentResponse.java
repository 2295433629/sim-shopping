package com.sim.shopping.interfaces.dto.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Payment响应对象，封装接口出参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class PaymentResponse {
    private String paymentNo;
    private String orderNo;
    private BigDecimal amount;
    private String paymentMethod;
    private String status;
    private LocalDateTime paidAt;

    /**
     * 获取Payment No
     * @return 返回结果
     */
    public String getPaymentNo() { return this.paymentNo; }
    /**
     * set Payment No
     * @param paymentNo paymentNo
     */
    public void setPaymentNo(String paymentNo) { this.paymentNo = paymentNo; }
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
     * 获取Amount
     * @return 返回结果
     */
    public BigDecimal getAmount() { return this.amount; }
    /**
     * set Amount
     * @param amount amount
     */
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    /**
     * 获取Payment Method
     * @return 返回结果
     */
    public String getPaymentMethod() { return this.paymentMethod; }
    /**
     * set Payment Method
     * @param paymentMethod paymentMethod
     */
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
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
    /**
     * 获取Paid At
     * @return 返回结果
     */
    public LocalDateTime getPaidAt() { return this.paidAt; }
    /**
     * set Paid At
     * @param paidAt paidAt
     */
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }
}
