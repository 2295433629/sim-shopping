package com.sim.shopping.interfaces.system;

import com.sim.shopping.application.system.PermissionService;
import com.sim.shopping.infrastructure.persistence.entity.SysPermissionDO;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/admin/permissions")
@PreAuthorize("hasRole('ADMIN')")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    public ApiResponse<PageResponse<SysPermissionDO>> getPermissions(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(permissionService.getPermissions(page, size));
    }

    @PostMapping
    public ApiResponse<SysPermissionDO> createPermission(@RequestBody SysPermissionDO permission) {
        return ApiResponse.success(permissionService.createPermission(permission));
    }

    @PutMapping("/{permissionId}")
    public ApiResponse<SysPermissionDO> updatePermission(@PathVariable Long permissionId, @RequestBody SysPermissionDO permission) {
        return ApiResponse.success(permissionService.updatePermission(permissionId, permission));
    }

    @DeleteMapping("/{permissionId}")
    public ApiResponse<Void> deletePermission(@PathVariable Long permissionId) {
        permissionService.deletePermission(permissionId);
        return ApiResponse.success(null);
    }
}
