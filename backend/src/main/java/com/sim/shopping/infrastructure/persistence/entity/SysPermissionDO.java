package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * SysPermission数据实体，对应数据库表
 *
 * @author Sim Team
 * @since 1.0.0
 */
@TableName("t_sys_permission")
public class SysPermissionDO extends BaseEntity {
    private Integer permissionType;
    private String description;
    private String module;
    private String permissionName;
    private String permissionCode;

    /**
     * 获取Permission Type
     * @return 返回结果
     */
    public Integer getPermissionType() { return this.permissionType; }
    /**
     * set Permission Type
     * @param permissionType permissionType
     */
    public void setPermissionType(Integer permissionType) { this.permissionType = permissionType; }
    /**
     * 获取Description
     * @return 返回结果
     */
    public String getDescription() { return this.description; }
    /**
     * set Description
     * @param description description
     */
    public void setDescription(String description) { this.description = description; }
    /**
     * 获取Module
     * @return 返回结果
     */
    public String getModule() { return this.module; }
    /**
     * set Module
     * @param module module
     */
    public void setModule(String module) { this.module = module; }
    /**
     * 获取Permission Name
     * @return 返回结果
     */
    public String getPermissionName() { return this.permissionName; }
    /**
     * set Permission Name
     * @param permissionName permissionName
     */
    public void setPermissionName(String permissionName) { this.permissionName = permissionName; }
    /**
     * 获取Permission Code
     * @return 返回结果
     */
    public String getPermissionCode() { return this.permissionCode; }
    /**
     * set Permission Code
     * @param permissionCode permissionCode
     */
    public void setPermissionCode(String permissionCode) { this.permissionCode = permissionCode; }
}
