package com.sim.shopping.interfaces.dto.refund;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class RefundRequest {

    @NotBlank(message = "退款类型不能为空")
    private String refundType;

    @NotBlank(message = "退款原因不能为空")
    @Size(max = 255, message = "退款原因最多255字符")
    private String reason;

    @NotNull(message = "退款金额不能为空")
    private BigDecimal amount;

    public String getRefundType() { return refundType; }
    public void setRefundType(String refundType) { this.refundType = refundType; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
