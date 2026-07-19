package com.sim.shopping.interfaces.dto.system;

import jakarta.validation.constraints.NotBlank;

/**
 * 角色请求DTO，封装接口入参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class RoleRequest {

    @NotBlank(message = "角色名称不能为空")
    private String roleName;
    @NotBlank(message = "角色编码不能为空")
    private String roleCode;
    private String status;
    private String description;

    /** 获取Role Name */
    public String getRoleName() { return this.roleName; }
    /** set Role Name */
    public void setRoleName(String roleName) { this.roleName = roleName; }
    /** 获取Role Code */
    public String getRoleCode() { return this.roleCode; }
    /** set Role Code */
    public void setRoleCode(String roleCode) { this.roleCode = roleCode; }
    /** 获取Status */
    public String getStatus() { return this.status; }
    /** set Status */
    public void setStatus(String status) { this.status = status; }
    /** 获取Description */
    public String getDescription() { return this.description; }
    /** set Description */
    public void setDescription(String description) { this.description = description; }
}
