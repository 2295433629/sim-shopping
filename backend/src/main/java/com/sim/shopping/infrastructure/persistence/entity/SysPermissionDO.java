package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@TableName("t_sys_permission")
public class SysPermissionDO extends BaseEntity {
    private Integer permissionType;
    private String description;
    private String module;
    private String permissionName;
    private String permissionCode;

    public Integer getPermissionType() { return this.permissionType; }
    public void setPermissionType(Integer permissionType) { this.permissionType = permissionType; }
    public String getDescription() { return this.description; }
    public void setDescription(String description) { this.description = description; }
    public String getModule() { return this.module; }
    public void setModule(String module) { this.module = module; }
    public String getPermissionName() { return this.permissionName; }
    public void setPermissionName(String permissionName) { this.permissionName = permissionName; }
    public String getPermissionCode() { return this.permissionCode; }
    public void setPermissionCode(String permissionCode) { this.permissionCode = permissionCode; }
}
