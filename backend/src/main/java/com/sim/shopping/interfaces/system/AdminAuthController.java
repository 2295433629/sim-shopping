package com.sim.shopping.interfaces.system;

import com.sim.shopping.application.system.AdminAuthService;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.system.AdminLoginRequest;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    public AdminAuthController(AdminAuthService adminAuthService) {
        this.adminAuthService = adminAuthService;
    }

    @PostMapping("/login")
    @PermitAll
    public ApiResponse<Map<String, Object>> login(@Valid @RequestBody AdminLoginRequest req) {
        return ApiResponse.success(adminAuthService.adminLogin(req));
    }
}
