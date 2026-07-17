package com.sim.shopping.interfaces.dto.auth;

import jakarta.validation.constraints.NotBlank;

/**
 * 登录请求DTO，包含用户名和密码
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class LoginRequest {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 获取Username
     * @return 返回结果
     */
    public String getUsername() { return this.username; }
    /**
     * set Username
     * @param username username
     */
    public void setUsername(String username) { this.username = username; }
    /**
     * 获取Password
     * @return 返回结果
     */
    public String getPassword() { return this.password; }
    /**
     * set Password
     * @param password password
     */
    public void setPassword(String password) { this.password = password; }
}
