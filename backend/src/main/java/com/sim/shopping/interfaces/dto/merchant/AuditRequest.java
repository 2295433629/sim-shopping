package com.sim.shopping.interfaces.dto.merchant;

import jakarta.validation.constraints.NotBlank;

public class AuditRequest {

    @NotBlank(message = "审核拒绝原因不能为空")
    private String reason;

    public String getReason() { return this.reason; }
    public void setReason(String reason) { this.reason = reason; }
}
