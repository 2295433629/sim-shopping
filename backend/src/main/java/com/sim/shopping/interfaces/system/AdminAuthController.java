package com.sim.shopping.interfaces.system;

import com.sim.shopping.application.system.AdminAuthService;
import com.sim.shopping.infrastructure.aop.Log;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.system.AdminLoginRequest;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * AdminAuth控制器，处理相关业务请求
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/admin")
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    public AdminAuthController(AdminAuthService adminAuthService) {
        this.adminAuthService = adminAuthService;
    }

    /**
     * 用户登录
     * @param req req
     * @return 返回结果
     */
    @PostMapping("/login")
    @PermitAll
    @Log(module = "认证", type = "操作")
    public ApiResponse<Map<String, Object>> login(@Valid @RequestBody AdminLoginRequest req) {
        return ApiResponse.success(adminAuthService.adminLogin(req));
    }

    /**
     * 修改密码
     * @param userDetails userDetails
     * @param body body（oldPassword, newPassword）
     * @return 返回结果
     */
    @PutMapping("/auth/password")
    @Log(module = "认证", type = "修改")
    public ApiResponse<Void> changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                            @RequestBody Map<String, String> body) {
        String oldPassword = body.get("oldPassword");
        String newPassword = body.get("newPassword");
        adminAuthService.changePassword(userDetails.getUsername(), oldPassword, newPassword);
        return ApiResponse.success(null);
    }
}
