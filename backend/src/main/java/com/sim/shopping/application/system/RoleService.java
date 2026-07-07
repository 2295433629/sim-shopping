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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public PageResponse<SysRoleDO> getRoles(int page, int size) {
        Page<SysRoleDO> pageResult = sysRoleMapper.selectPage(
                new Page<>(page, size),
                Wrappers.<SysRoleDO>lambdaQuery().orderByDesc(SysRoleDO::getCreatedAt)
        );
        return PageResponse.of(pageResult.getRecords(), pageResult.getTotal(), page, size);
    }

    public SysRoleDO getRoleById(Long id) {
        SysRoleDO role = sysRoleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException(404, "角色不存在: " + id);
        }
        return role;
    }

    @Transactional
    public SysRoleDO createRole(SysRoleDO role) {
        // Check role code uniqueness
        Long count = sysRoleMapper.selectCount(
                Wrappers.<SysRoleDO>lambdaQuery().eq(SysRoleDO::getRoleCode, role.getRoleCode())
        );
        if (count > 0) {
            throw new BusinessException(400, "角色编码已存在: " + role.getRoleCode());
        }
        if (role.getStatus() == null) {
            role.setStatus("ACTIVE");
        }
        sysRoleMapper.insert(role);
        return role;
    }

    @Transactional
    public SysRoleDO updateRole(Long id, SysRoleDO role) {
        SysRoleDO existing = getRoleById(id);
        existing.setRoleName(role.getRoleName());
        existing.setDescription(role.getDescription());
        existing.setStatus(role.getStatus());
        sysRoleMapper.updateById(existing);
        return existing;
    }

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

    public List<SysPermissionDO> getRolePermissions(Long roleId) {
        getRoleById(roleId);
        List<Long> permissionIds = sysRolePermissionMapper.selectList(
                Wrappers.<SysRolePermissionDO>lambdaQuery().eq(SysRolePermissionDO::getRoleId, roleId)
        ).stream().map(SysRolePermissionDO::getPermissionId).collect(Collectors.toList());

        if (permissionIds.isEmpty()) {
            return new ArrayList<>();
        }
        return sysPermissionMapper.selectBatchIds(permissionIds);
    }

    public List<SysMenuDO> getRoleMenus(Long roleId) {
        getRoleById(roleId);
        List<Long> menuIds = sysRoleMenuMapper.selectList(
                Wrappers.<SysRoleMenuDO>lambdaQuery().eq(SysRoleMenuDO::getRoleId, roleId)
        ).stream().map(SysRoleMenuDO::getMenuId).collect(Collectors.toList());

        if (menuIds.isEmpty()) {
            return new ArrayList<>();
        }
        return sysMenuMapper.selectBatchIds(menuIds);
    }
}
