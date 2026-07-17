package com.sim.shopping.interfaces.system;

import com.sim.shopping.application.system.RoleService;
import com.sim.shopping.infrastructure.persistence.entity.SysMenuDO;
import com.sim.shopping.infrastructure.persistence.entity.SysPermissionDO;
import com.sim.shopping.infrastructure.persistence.entity.SysRoleDO;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;

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
    public ApiResponse<PageResponse<SysRoleDO>> getRoles(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(roleService.getRoles(page, size));
    }

    /**
     * 创建角色
     * @param role role
     * @return 返回结果
     */
    @PostMapping
    public ApiResponse<SysRoleDO> createRole(@RequestBody SysRoleDO role) {
        return ApiResponse.success(roleService.createRole(role));
    }

    /**
     * 更新角色
     * @param roleId roleId
     * @param role role
     * @return 返回结果
     */
    @PutMapping("/{roleId}")
    public ApiResponse<SysRoleDO> updateRole(@PathVariable Long roleId, @RequestBody SysRoleDO role) {
        return ApiResponse.success(roleService.updateRole(roleId, role));
    }

    /**
     * 删除角色
     * @param roleId roleId
     * @return 返回结果
     */
    @DeleteMapping("/{roleId}")
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
    public ApiResponse<List<SysPermissionDO>> getRolePermissions(@PathVariable Long roleId) {
        return ApiResponse.success(roleService.getRolePermissions(roleId));
    }

    /**
     * assign Permissions
     * @param roleId roleId
     * @param body body
     * @return 返回结果
     */
    @PutMapping("/{roleId}/permissions")
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
    public ApiResponse<List<SysMenuDO>> getRoleMenus(@PathVariable Long roleId) {
        return ApiResponse.success(roleService.getRoleMenus(roleId));
    }

    /**
     * assign Menus
     * @param roleId roleId
     * @param body body
     * @return 返回结果
     */
    @PutMapping("/{roleId}/menus")
    public ApiResponse<Void> assignMenus(@PathVariable Long roleId, @RequestBody Map<String, List<Long>> body) {
        roleService.assignMenus(roleId, body.get("menuIds"));
        return ApiResponse.success(null);
    }
}
