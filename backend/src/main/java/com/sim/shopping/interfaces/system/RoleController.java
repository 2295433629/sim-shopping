package com.sim.shopping.interfaces.system;

import com.sim.shopping.application.system.RoleService;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.system.MenuResponse;
import com.sim.shopping.interfaces.dto.system.PermissionResponse;
import com.sim.shopping.interfaces.dto.system.RoleRequest;
import com.sim.shopping.interfaces.dto.system.RoleResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import com.sim.shopping.infrastructure.aop.Log;

/**
 * 角色管理控制器，处理角色和权限分配
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/admin/roles")
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 查询角色列表
     * @return 返回结果
     */
    @GetMapping
    public ApiResponse<PageResponse<RoleResponse>> getRoles(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(roleService.getRoles(page, size));
    }

    /**
     * 创建角色
     * @param request request
     * @return 返回结果
     */
    @PostMapping
    @Log(module = "系统", type = "新增")
    public ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest request) {
        return ApiResponse.success(roleService.createRole(request));
    }

    /**
     * 更新角色
     * @param roleId roleId
     * @param request request
     * @return 返回结果
     */
    @PutMapping("/{roleId}")
    @Log(module = "系统", type = "修改")
    public ApiResponse<RoleResponse> updateRole(@PathVariable Long roleId, @RequestBody RoleRequest request) {
        return ApiResponse.success(roleService.updateRole(roleId, request));
    }

    /**
     * 删除角色
     * @param roleId roleId
     * @return 返回结果
     */
    @DeleteMapping("/{roleId}")
    @Log(module = "系统", type = "删除")
    public ApiResponse<Void> deleteRole(@PathVariable Long roleId) {
        roleService.deleteRole(roleId);
        return ApiResponse.success(null);
    }

    /**
     * 获取Role Permissions
     * @param roleId roleId
     * @return 返回结果
     */
    @GetMapping("/{roleId}/permissions")
    public ApiResponse<List<PermissionResponse>> getRolePermissions(@PathVariable Long roleId) {
        return ApiResponse.success(roleService.getRolePermissions(roleId));
    }

    /**
     * assign Permissions
     * @param roleId roleId
     * @param body body
     * @return 返回结果
     */
    @PutMapping("/{roleId}/permissions")
    @Log(module = "系统", type = "修改")
    public ApiResponse<Void> assignPermissions(@PathVariable Long roleId, @RequestBody Map<String, List<Long>> body) {
        roleService.assignPermissions(roleId, body.get("permissionIds"));
        return ApiResponse.success(null);
    }

    /**
     * 获取Role Menus
     * @param roleId roleId
     * @return 返回结果
     */
    @GetMapping("/{roleId}/menus")
    public ApiResponse<List<MenuResponse>> getRoleMenus(@PathVariable Long roleId) {
        return ApiResponse.success(roleService.getRoleMenus(roleId));
    }

    /**
     * assign Menus
     * @param roleId roleId
     * @param body body
     * @return 返回结果
     */
    @PutMapping("/{roleId}/menus")
    @Log(module = "系统", type = "修改")
    public ApiResponse<Void> assignMenus(@PathVariable Long roleId, @RequestBody Map<String, List<Long>> body) {
        roleService.assignMenus(roleId, body.get("menuIds"));
        return ApiResponse.success(null);
    }
}
