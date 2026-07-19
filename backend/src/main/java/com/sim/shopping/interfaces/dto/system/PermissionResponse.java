package com.sim.shopping.interfaces.dto.system;

import java.time.LocalDateTime;

/**
 * 权限响应DTO，封装接口出参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class PermissionResponse {

    private Long id;
    private String permissionName;
    private String permissionCode;
    private Integer permissionType;
    private String description;
    private String module;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** 获取Id */
    public Long getId() { return this.id; }
    /** set Id */
    public void setId(Long id) { this.id = id; }
    /** 获取Permission Name */
    public String getPermissionName() { return this.permissionName; }
    /** set Permission Name */
    public void setPermissionName(String permissionName) { this.permissionName = permissionName; }
    /** 获取Permission Code */
    public String getPermissionCode() { return this.permissionCode; }
    /** set Permission Code */
    public void setPermissionCode(String permissionCode) { this.permissionCode = permissionCode; }
    /** 获取Permission Type */
    public Integer getPermissionType() { return this.permissionType; }
    /** set Permission Type */
    public void setPermissionType(Integer permissionType) { this.permissionType = permissionType; }
    /** 获取Description */
    public String getDescription() { return this.description; }
    /** set Description */
    public void setDescription(String description) { this.description = description; }
    /** 获取Module */
    public String getModule() { return this.module; }
    /** set Module */
    public void setModule(String module) { this.module = module; }
    /** 获取Created At */
    public LocalDateTime getCreatedAt() { return this.createdAt; }
    /** set Created At */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    /** 获取Updated At */
    public LocalDateTime getUpdatedAt() { return this.updatedAt; }
    /** set Updated At */
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
