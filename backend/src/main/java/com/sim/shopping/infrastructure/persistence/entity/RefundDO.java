package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 退款实体，对应 t_refund 表，存储退款申请记录
 *
 * @author Sim Team
 * @since 1.0.0
 */
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
     * 获取Refund Type
     * @return 返回结果
     */
    public String getRefundType() { return this.refundType; }
    /**
     * set Refund Type
     * @param refundType refundType
     */
    public void setRefundType(String refundType) { this.refundType = refundType; }
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
     * 获取Reason
     * @return 返回结果
     */
    public String getReason() { return this.reason; }
    /**
     * set Reason
     * @param reason reason
     */
    public void setReason(String reason) { this.reason = reason; }
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
     * 获取Shop Response
     * @return 返回结果
     */
    public String getShopResponse() { return this.shopResponse; }
    /**
     * set Shop Response
     * @param shopResponse shopResponse
     */
    public void setShopResponse(String shopResponse) { this.shopResponse = shopResponse; }
    /**
     * 获取Handled At
     * @return 返回结果
     */
    public LocalDateTime getHandledAt() { return this.handledAt; }
    /**
     * set Handled At
     * @param handledAt handledAt
     */
    public void setHandledAt(LocalDateTime handledAt) { this.handledAt = handledAt; }
    /**
     * 获取Completed At
     * @return 返回结果
     */
    public LocalDateTime getCompletedAt() { return this.completedAt; }
    /**
     * set Completed At
     * @param completedAt completedAt
     */
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}
