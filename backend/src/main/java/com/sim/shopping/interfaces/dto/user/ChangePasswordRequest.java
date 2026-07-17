package com.sim.shopping.interfaces.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * ChangePassword请求对象，封装接口入参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class ChangePasswordRequest {

    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 50, message = "新密码长度必须在6-50之间")
    private String newPassword;

    /**
     * 获取Old Password
     * @return 返回结果
     */
    public String getOldPassword() { return this.oldPassword; }
    /**
     * set Old Password
     * @param oldPassword oldPassword
     */
    public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }
    /**
     * 获取New Password
     * @return 返回结果
     */
    public String getNewPassword() { return this.newPassword; }
    /**
     * set New Password
     * @param newPassword newPassword
     */
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
