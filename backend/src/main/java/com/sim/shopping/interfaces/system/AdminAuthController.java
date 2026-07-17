package com.sim.shopping.interfaces.system;

import com.sim.shopping.application.system.AdminAuthService;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.system.AdminLoginRequest;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
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
@PreAuthorize("hasRole('ADMIN')")
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
    public ApiResponse<Map<String, Object>> login(@Valid @RequestBody AdminLoginRequest req) {
        return ApiResponse.success(adminAuthService.adminLogin(req));
    }
}
