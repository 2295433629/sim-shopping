package com.sim.shopping.interfaces.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChangePasswordRequest {

    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 50, message = "新密码长度必须在6-50之间")
    private String newPassword;

    public String getOldPassword() { return this.oldPassword; }
    public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }
    public String getNewPassword() { return this.newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
