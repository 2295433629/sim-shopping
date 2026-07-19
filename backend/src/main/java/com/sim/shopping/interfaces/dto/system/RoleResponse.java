package com.sim.shopping.interfaces.dto.system;

import java.time.LocalDateTime;

/**
 * 角色响应DTO，封装接口出参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class RoleResponse {

    private Long id;
    private String roleName;
    private String roleCode;
    private String status;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** 获取Id */
    public Long getId() { return this.id; }
    /** set Id */
    public void setId(Long id) { this.id = id; }
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
    /** 获取Created At */
    public LocalDateTime getCreatedAt() { return this.createdAt; }
    /** set Created At */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    /** 获取Updated At */
    public LocalDateTime getUpdatedAt() { return this.updatedAt; }
    /** set Updated At */
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
