package com.sim.shopping.application.system;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.infrastructure.persistence.entity.SysPermissionDO;
import com.sim.shopping.infrastructure.persistence.mapper.SysPermissionMapper;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {

    private final SysPermissionMapper sysPermissionMapper;

    public PermissionService(SysPermissionMapper sysPermissionMapper) {
        this.sysPermissionMapper = sysPermissionMapper;
    }

    public PageResponse<SysPermissionDO> getPermissions(int page, int size) {
        Page<SysPermissionDO> pageResult = sysPermissionMapper.selectPage(
                new Page<>(page, size),
                Wrappers.<SysPermissionDO>lambdaQuery().orderByDesc(SysPermissionDO::getCreatedAt)
        );
        return PageResponse.of(pageResult.getRecords(), pageResult.getTotal(), page, size);
    }

    public SysPermissionDO getPermissionById(Long id) {
        SysPermissionDO permission = sysPermissionMapper.selectById(id);
        if (permission == null) {
            throw new BusinessException(404, "权限不存在: " + id);
        }
        return permission;
    }

    public List<SysPermissionDO> getAllPermissions() {
        return sysPermissionMapper.selectList(null);
    }

    public List<SysPermissionDO> getPermissionsByModule(String module) {
        return sysPermissionMapper.selectList(
                Wrappers.<SysPermissionDO>lambdaQuery().eq(SysPermissionDO::getModule, module)
        );
    }

    public SysPermissionDO createPermission(SysPermissionDO permission) {
        // Check permission code uniqueness
        Long count = sysPermissionMapper.selectCount(
                Wrappers.<SysPermissionDO>lambdaQuery().eq(SysPermissionDO::getPermissionCode, permission.getPermissionCode())
        );
        if (count > 0) {
            throw new BusinessException(400, "权限编码已存在: " + permission.getPermissionCode());
        }
        sysPermissionMapper.insert(permission);
        return permission;
    }

    public SysPermissionDO updatePermission(Long id, SysPermissionDO permission) {
        SysPermissionDO existing = getPermissionById(id);
        existing.setPermissionName(permission.getPermissionName());
        existing.setDescription(permission.getDescription());
        existing.setModule(permission.getModule());
        existing.setPermissionType(permission.getPermissionType());
        sysPermissionMapper.updateById(existing);
        return existing;
    }

    public void deletePermission(Long id) {
        getPermissionById(id);
        sysPermissionMapper.deleteById(id);
    }
}
