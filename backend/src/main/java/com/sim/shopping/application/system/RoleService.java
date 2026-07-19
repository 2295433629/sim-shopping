package com.sim.shopping.application.system;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.infrastructure.persistence.entity.SysMenuDO;
import com.sim.shopping.infrastructure.persistence.entity.SysPermissionDO;
import com.sim.shopping.infrastructure.persistence.entity.SysRoleDO;
import com.sim.shopping.infrastructure.persistence.entity.SysRoleMenuDO;
import com.sim.shopping.infrastructure.persistence.entity.SysRolePermissionDO;
import com.sim.shopping.infrastructure.persistence.mapper.SysMenuMapper;
import com.sim.shopping.infrastructure.persistence.mapper.SysPermissionMapper;
import com.sim.shopping.infrastructure.persistence.mapper.SysRoleMapper;
import com.sim.shopping.infrastructure.persistence.mapper.SysRoleMenuMapper;
import com.sim.shopping.infrastructure.persistence.mapper.SysRolePermissionMapper;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.system.MenuResponse;
import com.sim.shopping.interfaces.dto.system.PermissionResponse;
import com.sim.shopping.interfaces.dto.system.RoleRequest;
import com.sim.shopping.interfaces.dto.system.RoleResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色服务，处理角色的增删改查和权限分配
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Service
public class RoleService {

    private final SysRoleMapper sysRoleMapper;
    private final SysRolePermissionMapper sysRolePermissionMapper;
    private final SysRoleMenuMapper sysRoleMenuMapper;
    private final SysPermissionMapper sysPermissionMapper;
    private final SysMenuMapper sysMenuMapper;

    public RoleService(SysRoleMapper sysRoleMapper,
                       SysRolePermissionMapper sysRolePermissionMapper,
                       SysRoleMenuMapper sysRoleMenuMapper,
                       SysPermissionMapper sysPermissionMapper,
                       SysMenuMapper sysMenuMapper) {
        this.sysRoleMapper = sysRoleMapper;
        this.sysRolePermissionMapper = sysRolePermissionMapper;
        this.sysRoleMenuMapper = sysRoleMenuMapper;
        this.sysPermissionMapper = sysPermissionMapper;
        this.sysMenuMapper = sysMenuMapper;
    }

    /**
     * 查询角色列表
     * @param page page
     * @param size size
     * @return 返回结果
     */
    public PageResponse<RoleResponse> getRoles(int page, int size) {
        Page<SysRoleDO> pageResult = sysRoleMapper.selectPage(
                new Page<>(page, size),
                Wrappers.<SysRoleDO>lambdaQuery().orderByDesc(SysRoleDO::getCreatedAt)
        );
        List<RoleResponse> records = pageResult.getRecords().stream()
                .map(this::toRoleResponse)
                .collect(Collectors.toList());
        return PageResponse.of(records, pageResult.getTotal(), page, size);
    }

    /**
     * 获取Role By Id
     * @param id id
     * @return 返回结果
     */
    public SysRoleDO getRoleById(Long id) {
        SysRoleDO role = sysRoleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException(404, "角色不存在: " + id);
        }
        return role;
    }

    /**
     * 创建角色
     * @param request request
     * @return 返回结果
     */
    @Transactional
    public RoleResponse createRole(RoleRequest request) {
        // Check role code uniqueness
        Long count = sysRoleMapper.selectCount(
                Wrappers.<SysRoleDO>lambdaQuery().eq(SysRoleDO::getRoleCode, request.getRoleCode())
        );
        if (count > 0) {
            throw new BusinessException(400, "角色编码已存在: " + request.getRoleCode());
        }
        SysRoleDO role = toEntity(request);
        if (role.getStatus() == null) {
            role.setStatus("ACTIVE");
        }
        sysRoleMapper.insert(role);
        return toRoleResponse(role);
    }

    /**
     * 更新角色
     * @param id id
     * @param request request
     * @return 返回结果
     */
    @Transactional
    public RoleResponse updateRole(Long id, RoleRequest request) {
        SysRoleDO existing = getRoleById(id);
        existing.setRoleName(request.getRoleName());
        existing.setDescription(request.getDescription());
        existing.setStatus(request.getStatus());
        sysRoleMapper.updateById(existing);
        return toRoleResponse(existing);
    }

    /**
     * 删除角色
     * @param id id
     */
    @Transactional
    public void deleteRole(Long id) {
        getRoleById(id);
        // Remove associated permissions and menus
        sysRolePermissionMapper.delete(
                Wrappers.<SysRolePermissionDO>lambdaQuery().eq(SysRolePermissionDO::getRoleId, id)
        );
        sysRoleMenuMapper.delete(
                Wrappers.<SysRoleMenuDO>lambdaQuery().eq(SysRoleMenuDO::getRoleId, id)
        );
        sysRoleMapper.deleteById(id);
    }

    /**
     * assign Permissions
     * @param roleId roleId
     * @param permissionIds permissionIds
     */
    @Transactional
    public void assignPermissions(Long roleId, List<Long> permissionIds) {
        getRoleById(roleId);
        // Remove existing permissions
        sysRolePermissionMapper.delete(
                Wrappers.<SysRolePermissionDO>lambdaQuery().eq(SysRolePermissionDO::getRoleId, roleId)
        );
        // Insert new permissions
        if (permissionIds != null) {
            for (Long permissionId : permissionIds) {
                SysRolePermissionDO rp = new SysRolePermissionDO();
                rp.setRoleId(roleId);
                rp.setPermissionId(permissionId);
                sysRolePermissionMapper.insert(rp);
            }
        }
    }

    /**
     * assign Menus
     * @param roleId roleId
     * @param menuIds menuIds
     */
    @Transactional
    public void assignMenus(Long roleId, List<Long> menuIds) {
        getRoleById(roleId);
        // Remove existing menus
        sysRoleMenuMapper.delete(
                Wrappers.<SysRoleMenuDO>lambdaQuery().eq(SysRoleMenuDO::getRoleId, roleId)
        );
        // Insert new menus
        if (menuIds != null) {
            for (Long menuId : menuIds) {
                SysRoleMenuDO rm = new SysRoleMenuDO();
                rm.setRoleId(roleId);
                rm.setMenuId(menuId);
                sysRoleMenuMapper.insert(rm);
            }
        }
    }

    /**
     * 获取Role Permissions
     * @param roleId roleId
     * @return 返回结果
     */
    public List<PermissionResponse> getRolePermissions(Long roleId) {
        getRoleById(roleId);
        List<Long> permissionIds = sysRolePermissionMapper.selectList(
                Wrappers.<SysRolePermissionDO>lambdaQuery().eq(SysRolePermissionDO::getRoleId, roleId)
        ).stream().map(SysRolePermissionDO::getPermissionId).collect(Collectors.toList());

        if (permissionIds.isEmpty()) {
            return new ArrayList<>();
        }
        return sysPermissionMapper.selectBatchIds(permissionIds).stream()
                .map(this::toPermissionResponse)
                .collect(Collectors.toList());
    }

    /**
     * 获取Role Menus
     * @param roleId roleId
     * @return 返回结果
     */
    public List<MenuResponse> getRoleMenus(Long roleId) {
        getRoleById(roleId);
        List<Long> menuIds = sysRoleMenuMapper.selectList(
                Wrappers.<SysRoleMenuDO>lambdaQuery().eq(SysRoleMenuDO::getRoleId, roleId)
        ).stream().map(SysRoleMenuDO::getMenuId).collect(Collectors.toList());

        if (menuIds.isEmpty()) {
            return new ArrayList<>();
        }
        return sysMenuMapper.selectBatchIds(menuIds).stream()
                .map(this::toMenuResponse)
                .collect(Collectors.toList());
    }

    private SysRoleDO toEntity(RoleRequest request) {
        SysRoleDO role = new SysRoleDO();
        role.setRoleName(request.getRoleName());
        role.setRoleCode(request.getRoleCode());
        role.setStatus(request.getStatus());
        role.setDescription(request.getDescription());
        return role;
    }

    private RoleResponse toRoleResponse(SysRoleDO role) {
        RoleResponse resp = new RoleResponse();
        resp.setId(role.getId());
        resp.setRoleName(role.getRoleName());
        resp.setRoleCode(role.getRoleCode());
        resp.setStatus(role.getStatus());
        resp.setDescription(role.getDescription());
        resp.setCreatedAt(role.getCreatedAt());
        resp.setUpdatedAt(role.getUpdatedAt());
        return resp;
    }

    private PermissionResponse toPermissionResponse(SysPermissionDO permission) {
        PermissionResponse resp = new PermissionResponse();
        resp.setId(permission.getId());
        resp.setPermissionName(permission.getPermissionName());
        resp.setPermissionCode(permission.getPermissionCode());
        resp.setPermissionType(permission.getPermissionType());
        resp.setDescription(permission.getDescription());
        resp.setModule(permission.getModule());
        resp.setCreatedAt(permission.getCreatedAt());
        resp.setUpdatedAt(permission.getUpdatedAt());
        return resp;
    }

    private MenuResponse toMenuResponse(SysMenuDO menu) {
        MenuResponse resp = new MenuResponse();
        resp.setId(menu.getId());
        resp.setMenuName(menu.getName());
        resp.setParentId(menu.getParentId());
        resp.setMenuType(menu.getType());
        resp.setPath(menu.getPath());
        resp.setComponent(menu.getComponent());
        resp.setIcon(menu.getIcon());
        resp.setSortOrder(menu.getSortOrder());
        resp.setVisible(menu.getVisible());
        resp.setCreatedAt(menu.getCreatedAt());
        return resp;
    }
}
