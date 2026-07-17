package com.sim.shopping.interfaces.dto.auth;

import jakarta.validation.constraints.NotBlank;

/**
 * RefreshToken请求对象，封装接口入参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class RefreshTokenRequest {

    @NotBlank(message = "refreshToken不能为空")
    private String refreshToken;

    /**
     * 获取Refresh Token
     * @return 返回结果
     */
    public String getRefreshToken() { return this.refreshToken; }
    /**
     * set Refresh Token
     * @param refreshToken refreshToken
     */
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
}
