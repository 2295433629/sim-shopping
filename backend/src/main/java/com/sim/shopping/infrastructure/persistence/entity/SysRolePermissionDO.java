package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@TableName("t_sys_role_permission")
public class SysRolePermissionDO extends BaseEntity {
    private Long roleId;
    private Long permissionId;

    public Long getRoleId() { return this.roleId; }
    public void setRoleId(Long roleId) { this.roleId = roleId; }
    public Long getPermissionId() { return this.permissionId; }
    public void setPermissionId(Long permissionId) { this.permissionId = permissionId; }
}
