package com.sim.shopping.interfaces.dto.refund;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 退款视图对象，包含退款申请详情
 *
 * @author Sim Team
 * @since 1.0.0
 */
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

    /**
     * 获取Refund Id
     * @return 返回结果
     */
    public Long getRefundId() { return refundId; }
    /**
     * set Refund Id
     * @param refundId refundId
     */
    public void setRefundId(Long refundId) { this.refundId = refundId; }
    /**
     * 获取Order No
     * @return 返回结果
     */
    public String getOrderNo() { return orderNo; }
    /**
     * set Order No
     * @param orderNo orderNo
     */
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    /**
     * 获取Refund Type
     * @return 返回结果
     */
    public String getRefundType() { return refundType; }
    /**
     * set Refund Type
     * @param refundType refundType
     */
    public void setRefundType(String refundType) { this.refundType = refundType; }
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
     * 获取Reason
     * @return 返回结果
     */
    public String getReason() { return reason; }
    /**
     * set Reason
     * @param reason reason
     */
    public void setReason(String reason) { this.reason = reason; }
    /**
     * 获取Amount
     * @return 返回结果
     */
    public BigDecimal getAmount() { return amount; }
    /**
     * set Amount
     * @param amount amount
     */
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    /**
     * 获取Shop Response
     * @return 返回结果
     */
    public String getShopResponse() { return shopResponse; }
    /**
     * set Shop Response
     * @param shopResponse shopResponse
     */
    public void setShopResponse(String shopResponse) { this.shopResponse = shopResponse; }
    /**
     * 获取Handled At
     * @return 返回结果
     */
    public LocalDateTime getHandledAt() { return handledAt; }
    /**
     * set Handled At
     * @param handledAt handledAt
     */
    public void setHandledAt(LocalDateTime handledAt) { this.handledAt = handledAt; }
    /**
     * 获取Completed At
     * @return 返回结果
     */
    public LocalDateTime getCompletedAt() { return completedAt; }
    /**
     * set Completed At
     * @param completedAt completedAt
     */
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    /** 获取Created At */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /** set Created At */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
