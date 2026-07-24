package com.sim.shopping.application.auth;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sim.shopping.infrastructure.common.SystemConstants;
import com.sim.shopping.infrastructure.persistence.entity.SysPermissionDO;
import com.sim.shopping.infrastructure.persistence.entity.SysRoleDO;
import com.sim.shopping.infrastructure.persistence.entity.SysRolePermissionDO;
import com.sim.shopping.infrastructure.persistence.mapper.SysPermissionMapper;
import com.sim.shopping.infrastructure.persistence.mapper.SysRoleMapper;
import com.sim.shopping.infrastructure.persistence.mapper.SysRolePermissionMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Rbac服务，处理相关业务逻辑
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Service
public class RbacService {

    private final SysRoleMapper sysRoleMapper;
    private final SysPermissionMapper sysPermissionMapper;
    private final SysRolePermissionMapper sysRolePermissionMapper;

    public RbacService(SysRoleMapper sysRoleMapper,
                       SysPermissionMapper sysPermissionMapper,
                       SysRolePermissionMapper sysRolePermissionMapper) {
        this.sysRoleMapper = sysRoleMapper;
        this.sysPermissionMapper = sysPermissionMapper;
        this.sysRolePermissionMapper = sysRolePermissionMapper;
    }

    /**
     * Get permission codes by role code.
     */
    public List<String> getPermissionCodesByRoleCode(String roleCode) {
        SysRoleDO role = sysRoleMapper.selectOne(
                Wrappers.<SysRoleDO>lambdaQuery().eq(SysRoleDO::getRoleCode, roleCode).last("LIMIT 1")
        );
        if (role == null) {
            return Collections.emptyList();
        }

        List<Long> permissionIds = sysRolePermissionMapper.selectList(
                Wrappers.<SysRolePermissionDO>lambdaQuery().eq(SysRolePermissionDO::getRoleId, role.getId())
        ).stream().map(SysRolePermissionDO::getPermissionId).collect(Collectors.toList());

        if (permissionIds.isEmpty()) {
            return Collections.emptyList();
        }

        return sysPermissionMapper.selectBatchIds(permissionIds).stream()
                .map(SysPermissionDO::getPermissionCode)
                .collect(Collectors.toList());
    }

    /**
     * Get all active roles.
     */
    public List<SysRoleDO> getAllActiveRoles() {
        return sysRoleMapper.selectList(
                Wrappers.<SysRoleDO>lambdaQuery().eq(SysRoleDO::getStatus, SystemConstants.STATUS_ACTIVE)
        );
    }

    /**
     * Get all permissions.
     */
    public List<SysPermissionDO> getAllPermissions() {
        return sysPermissionMapper.selectList(null);
    }
}
