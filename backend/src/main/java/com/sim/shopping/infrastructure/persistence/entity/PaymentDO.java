package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * Payment数据实体，对应数据库表
 *
 * @author Sim Team
 * @since 1.0.0
 */
@TableName("t_payment")
public class PaymentDO extends BaseEntity {
    private Long orderId;
    private LocalDateTime paidAt;
    private String orderNo;
    private String paymentNo;
    private String paymentMethod;
    private BigDecimal amount;
    private String status;

    /**
     * 获取Order Id
     * @return 返回结果
     */
    public Long getOrderId() { return this.orderId; }
    /**
     * set Order Id
     * @param orderId orderId
     */
    public void setOrderId(Long orderId) { this.orderId = orderId; }
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
