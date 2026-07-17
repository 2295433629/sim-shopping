package com.sim.shopping.interfaces.auth;

import com.sim.shopping.application.auth.AuthService;
import com.sim.shopping.application.system.LoginLogService;
import com.sim.shopping.interfaces.dto.auth.LoginRequest;
import com.sim.shopping.interfaces.dto.auth.RefreshTokenRequest;
import com.sim.shopping.interfaces.dto.auth.RegisterRequest;
import com.sim.shopping.interfaces.dto.auth.TokenResponse;
import com.sim.shopping.interfaces.dto.auth.UserInfoResponse;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证控制器，处理用户登录、注册、登出、Token刷新
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/common")
public class AuthController {

    private final AuthService authService;
    private final LoginLogService loginLogService;

    public AuthController(AuthService authService, LoginLogService loginLogService) {
        this.authService = authService;
        this.loginLogService = loginLogService;
    }

    /**
     * 用户注册
     * @param request request
     * @return 返回结果
     */
    @PostMapping("/register")
    public ApiResponse<TokenResponse> register(@Valid @RequestBody RegisterRequest request) {
        TokenResponse tokenResponse = authService.register(request);
        return ApiResponse.success(tokenResponse);
    }

    /**
     * 用户登录
     * @return 返回结果
     */
    @PostMapping("/login")
    public ApiResponse<TokenResponse> login(@Valid @RequestBody LoginRequest request,
                                            HttpServletRequest httpRequest) {
        String ip = httpRequest.getRemoteAddr();
        String userAgent = httpRequest.getHeader("User-Agent");
        try {
            TokenResponse tokenResponse = authService.login(request);
            loginLogService.recordLoginLog(
                    tokenResponse.getUserId(), request.getUsername(),
                    tokenResponse.getRole(), 0, ip, userAgent, null);
            return ApiResponse.success(tokenResponse);
        } catch (Exception e) {
            loginLogService.recordLoginLog(
                    null, request.getUsername(), "USER", 1, ip, userAgent, e.getMessage());
            throw e;
        }
    }

    /**
     * 用户登出
     * @return 返回结果
     */
    @PostMapping("/logout")
    public ApiResponse<Void> logout() {
        authService.logout();
        return ApiResponse.success();
    }

    /**
     * 刷新Token
     * @param request request
     * @return 返回结果
     */
    @PostMapping("/refresh-token")
    public ApiResponse<TokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        TokenResponse tokenResponse = authService.refreshToken(request.getRefreshToken());
        return ApiResponse.success(tokenResponse);
    }

    /**
     * 获取用户信息
     * @return 返回结果
     */
    @GetMapping("/userinfo")
    public ApiResponse<UserInfoResponse> getUserInfo() {
        UserInfoResponse userInfo = authService.getUserInfo();
        return ApiResponse.success(userInfo);
    }
}
