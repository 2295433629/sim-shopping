package com.sim.shopping.interfaces.dto.merchant;

import jakarta.validation.constraints.NotBlank;

/**
 * Audit请求对象，封装接口入参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class AuditRequest {

    @NotBlank(message = "审核拒绝原因不能为空")
    private String reason;

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
}
