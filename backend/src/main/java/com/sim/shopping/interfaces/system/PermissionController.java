package com.sim.shopping.interfaces.system;

import com.sim.shopping.application.system.PermissionService;
import com.sim.shopping.infrastructure.persistence.entity.SysPermissionDO;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ApiResponse<PageResponse<SysPermissionDO>> getPermissions(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(permissionService.getPermissions(page, size));
    }

    /**
     * 创建Permission
     * @param permission permission
     * @return 返回结果
     */
    @PostMapping
    public ApiResponse<SysPermissionDO> createPermission(@RequestBody SysPermissionDO permission) {
        return ApiResponse.success(permissionService.createPermission(permission));
    }

    /**
     * 更新Permission
     * @param permissionId permissionId
     * @param permission permission
     * @return 返回结果
     */
    @PutMapping("/{permissionId}")
    public ApiResponse<SysPermissionDO> updatePermission(@PathVariable Long permissionId, @RequestBody SysPermissionDO permission) {
        return ApiResponse.success(permissionService.updatePermission(permissionId, permission));
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
