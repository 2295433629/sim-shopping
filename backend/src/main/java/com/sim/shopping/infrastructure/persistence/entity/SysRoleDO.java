package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@TableName("t_sys_role")
public class SysRoleDO extends BaseEntity {
    private String roleCode;
    private String status;
    private String description;
    private String roleName;

    public String getRoleCode() { return this.roleCode; }
    public void setRoleCode(String roleCode) { this.roleCode = roleCode; }
    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }
    public String getDescription() { return this.description; }
    public void setDescription(String description) { this.description = description; }
    public String getRoleName() { return this.roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
}
