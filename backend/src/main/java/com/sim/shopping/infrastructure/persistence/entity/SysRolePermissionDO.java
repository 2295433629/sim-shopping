package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * SysRolePermission数据实体，对应数据库表
 *
 * @author Sim Team
 * @since 1.0.0
 */
@TableName("t_sys_role_permission")
public class SysRolePermissionDO extends BaseEntity {
    private Long roleId;
    private Long permissionId;

    /**
     * 获取Role Id
     * @return 返回结果
     */
    public Long getRoleId() { return this.roleId; }
    /**
     * set Role Id
     * @param roleId roleId
     */
    public void setRoleId(Long roleId) { this.roleId = roleId; }
    /**
     * 获取Permission Id
     * @return 返回结果
     */
    public Long getPermissionId() { return this.permissionId; }
    /**
     * set Permission Id
     * @param permissionId permissionId
     */
    public void setPermissionId(Long permissionId) { this.permissionId = permissionId; }
}
