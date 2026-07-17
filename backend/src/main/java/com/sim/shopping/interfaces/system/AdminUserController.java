package com.sim.shopping.interfaces.system;

import com.sim.shopping.application.system.AdminUserService;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.system.AdminUserItem;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * AdminUser控制器，处理相关业务请求
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    /**
     * 获取User List
     * @return 返回结果
     */
    @GetMapping("/users")
    public ApiResponse<PageResponse<AdminUserItem>> getUserList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status) {
        return ApiResponse.success(adminUserService.getUserList(page, size, keyword, status));
    }

    /**
     * 获取User Detail
     * @param userId userId
     * @return 返回结果
     */
    @GetMapping("/users/{userId}")
    public ApiResponse<AdminUserItem> getUserDetail(@PathVariable Long userId) {
        return ApiResponse.success(adminUserService.getUserDetail(userId));
    }

    /**
     * 禁用User
     * @param userId userId
     * @return 返回结果
     */
    @PatchMapping("/users/{userId}/disable")
    public ApiResponse<Void> disableUser(@PathVariable Long userId) {
        adminUserService.disableUser(userId);
        return ApiResponse.success();
    }

    /**
     * 启用User
     * @param userId userId
     * @return 返回结果
     */
    @PatchMapping("/users/{userId}/enable")
    public ApiResponse<Void> enableUser(@PathVariable Long userId) {
        adminUserService.enableUser(userId);
        return ApiResponse.success();
    }
}
