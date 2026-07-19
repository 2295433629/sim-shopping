package com.sim.shopping.interfaces.dto.system;

import jakarta.validation.constraints.NotBlank;

/**
 * 权限请求DTO，封装接口入参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class PermissionRequest {

    @NotBlank(message = "权限名称不能为空")
    private String permissionName;
    @NotBlank(message = "权限编码不能为空")
    private String permissionCode;
    private Integer permissionType;
    private String description;
    private String module;

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
}
