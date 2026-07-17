package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * SysRole数据实体，对应数据库表
 *
 * @author Sim Team
 * @since 1.0.0
 */
@TableName("t_sys_role")
public class SysRoleDO extends BaseEntity {
    private String roleCode;
    private String status;
    private String description;
    private String roleName;

    /**
     * 获取Role Code
     * @return 返回结果
     */
    public String getRoleCode() { return this.roleCode; }
    /**
     * set Role Code
     * @param roleCode roleCode
     */
    public void setRoleCode(String roleCode) { this.roleCode = roleCode; }
    /**
     * 获取Status
     * @return 返回结果
     */
    public String getStatus() { return this.status; }
    /**
     * set Status
     * @param status status
     */
    public void setStatus(String status) { this.status = status; }
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
     * 获取Role Name
     * @return 返回结果
     */
    public String getRoleName() { return this.roleName; }
    /**
     * set Role Name
     * @param roleName roleName
     */
    public void setRoleName(String roleName) { this.roleName = roleName; }
}
