package com.sim.shopping.interfaces.dto.refund;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * 退款请求DTO，包含订单号和退款原因
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class RefundRequest {

    @NotBlank(message = "退款类型不能为空")
    private String refundType;

    @NotBlank(message = "退款原因不能为空")
    @Size(max = 255, message = "退款原因最多255字符")
    private String reason;

    @NotNull(message = "退款金额不能为空")
    private BigDecimal amount;

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
}
