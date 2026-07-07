package com.sim.shopping.interfaces.system;

import com.sim.shopping.application.system.AdminUserService;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.system.AdminUserItem;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping("/users")
    public ApiResponse<PageResponse<AdminUserItem>> getUserList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status) {
        return ApiResponse.success(adminUserService.getUserList(page, size, keyword, status));
    }

    @GetMapping("/users/{userId}")
    public ApiResponse<AdminUserItem> getUserDetail(@PathVariable Long userId) {
        return ApiResponse.success(adminUserService.getUserDetail(userId));
    }

    @PatchMapping("/users/{userId}/disable")
    public ApiResponse<Void> disableUser(@PathVariable Long userId) {
        adminUserService.disableUser(userId);
        return ApiResponse.success();
    }

    @PatchMapping("/users/{userId}/enable")
    public ApiResponse<Void> enableUser(@PathVariable Long userId) {
        adminUserService.enableUser(userId);
        return ApiResponse.success();
    }
}
