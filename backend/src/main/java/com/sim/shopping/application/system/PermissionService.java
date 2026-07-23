package com.sim.shopping.application.system;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.infrastructure.persistence.entity.SysPermissionDO;
import com.sim.shopping.infrastructure.persistence.entity.SysRolePermissionDO;
import com.sim.shopping.infrastructure.persistence.mapper.SysPermissionMapper;
import com.sim.shopping.infrastructure.persistence.mapper.SysRolePermissionMapper;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.system.PermissionRequest;
import com.sim.shopping.interfaces.dto.system.PermissionResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限服务，处理权限的增删改查
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Service
public class PermissionService {

    private final SysPermissionMapper sysPermissionMapper;
    private final SysRolePermissionMapper sysRolePermissionMapper;

    public PermissionService(SysPermissionMapper sysPermissionMapper,
                             SysRolePermissionMapper sysRolePermissionMapper) {
        this.sysPermissionMapper = sysPermissionMapper;
        this.sysRolePermissionMapper = sysRolePermissionMapper;
    }

    /**
     * 查询权限列表
     * @param page page
     * @param size size
     * @return 返回结果
     */
    public PageResponse<PermissionResponse> getPermissions(int page, int size) {
        Page<SysPermissionDO> pageResult = sysPermissionMapper.selectPage(
                new Page<>(page, size),
                Wrappers.<SysPermissionDO>lambdaQuery().orderByDesc(SysPermissionDO::getCreatedAt)
        );
        List<PermissionResponse> records = pageResult.getRecords().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return PageResponse.of(records, pageResult.getTotal(), page, size);
    }

    /**
     * 获取Permission By Id
     * @param id id
     * @return 返回结果
     */
    public SysPermissionDO getPermissionById(Long id) {
        SysPermissionDO permission = sysPermissionMapper.selectById(id);
        if (permission == null) {
            throw new BusinessException(404, "权限不存在: " + id);
        }
        return permission;
    }

    /**
     * 获取All Permissions
     * @return 返回结果
     */
    public List<SysPermissionDO> getAllPermissions() {
        return sysPermissionMapper.selectList(null);
    }

    /**
     * 获取Permissions By Module
     * @param module module
     * @return 返回结果
     */
    public List<SysPermissionDO> getPermissionsByModule(String module) {
        return sysPermissionMapper.selectList(
                Wrappers.<SysPermissionDO>lambdaQuery().eq(SysPermissionDO::getModule, module)
        );
    }

    /**
     * 创建Permission
     * @param request request
     * @return 返回结果
     */
    public PermissionResponse createPermission(PermissionRequest request) {
        // Check permission code uniqueness
        Long count = sysPermissionMapper.selectCount(
                Wrappers.<SysPermissionDO>lambdaQuery().eq(SysPermissionDO::getPermissionCode, request.getPermissionCode())
        );
        if (count > 0) {
            throw new BusinessException(400, "权限编码已存在: " + request.getPermissionCode());
        }
        SysPermissionDO permission = toEntity(request);
        sysPermissionMapper.insert(permission);
        return toResponse(permission);
    }

    /**
     * 更新Permission
     * @param id id
     * @param request request
     * @return 返回结果
     */
    public PermissionResponse updatePermission(Long id, PermissionRequest request) {
        SysPermissionDO existing = getPermissionById(id);
        existing.setPermissionName(request.getPermissionName());
        existing.setDescription(request.getDescription());
        existing.setModule(request.getModule());
        existing.setPermissionType(request.getPermissionType());
        sysPermissionMapper.updateById(existing);
        return toResponse(existing);
    }

    /**
     * 删除Permission，同时清理角色-权限关联
     * @param id id
     */
    @Transactional
    public void deletePermission(Long id) {
        getPermissionById(id);
        // 清理角色-权限关联，避免孤儿引用
        sysRolePermissionMapper.delete(
                Wrappers.<SysRolePermissionDO>lambdaQuery().eq(SysRolePermissionDO::getPermissionId, id)
        );
        sysPermissionMapper.deleteById(id);
    }

    private SysPermissionDO toEntity(PermissionRequest request) {
        SysPermissionDO permission = new SysPermissionDO();
        permission.setPermissionName(request.getPermissionName());
        permission.setPermissionCode(request.getPermissionCode());
        permission.setPermissionType(request.getPermissionType());
        permission.setDescription(request.getDescription());
        permission.setModule(request.getModule());
        return permission;
    }

    private PermissionResponse toResponse(SysPermissionDO permission) {
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
}
