package com.sim.shopping.interfaces.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 注册请求DTO，包含用户名、密码、昵称等信息
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class RegisterRequest {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 20, message = "用户名长度4-20个字符")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度6-20个字符")
    private String password;

    @Size(max = 50, message = "昵称最多50个字符")
    private String nickname;

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
    /**
     * 获取Nickname
     * @return 返回结果
     */
    public String getNickname() { return this.nickname; }
    /**
     * set Nickname
     * @param nickname nickname
     */
    public void setNickname(String nickname) { this.nickname = nickname; }
}
