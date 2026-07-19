package com.sim.shopping.interfaces.system;

import com.sim.shopping.application.system.PermissionService;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.system.PermissionRequest;
import com.sim.shopping.interfaces.dto.system.PermissionResponse;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

/**
 * 权限管理控制器，处理权限的增删改查
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/admin/permissions")
@PreAuthorize("hasRole('ADMIN')")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    /**
     * 查询权限列表
     * @return 返回结果
     */
    @GetMapping
    public ApiResponse<PageResponse<PermissionResponse>> getPermissions(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(permissionService.getPermissions(page, size));
    }

    /**
     * 创建Permission
     * @param request request
     * @return 返回结果
     */
    @PostMapping
    public ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest request) {
        return ApiResponse.success(permissionService.createPermission(request));
    }

    /**
     * 更新Permission
     * @param permissionId permissionId
     * @param request request
     * @return 返回结果
     */
    @PutMapping("/{permissionId}")
    public ApiResponse<PermissionResponse> updatePermission(@PathVariable Long permissionId, @RequestBody PermissionRequest request) {
        return ApiResponse.success(permissionService.updatePermission(permissionId, request));
    }

    /**
     * 删除Permission
     * @param permissionId permissionId
     * @return 返回结果
     */
    @DeleteMapping("/{permissionId}")
    public ApiResponse<Void> deletePermission(@PathVariable Long permissionId) {
        permissionService.deletePermission(permissionId);
        return ApiResponse.success(null);
    }
}
