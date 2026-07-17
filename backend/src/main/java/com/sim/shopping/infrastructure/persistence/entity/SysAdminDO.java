package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * SysAdmin数据实体，对应数据库表
 *
 * @author Sim Team
 * @since 1.0.0
 */
@TableName("t_sys_admin")
public class SysAdminDO extends BaseEntity {
    private String adminName;
    private Long userId;
    private String role;

    /**
     * 获取Admin Name
     * @return 返回结果
     */
    public String getAdminName() { return this.adminName; }
    /**
     * set Admin Name
     * @param adminName adminName
     */
    public void setAdminName(String adminName) { this.adminName = adminName; }
    /**
     * 获取User Id
     * @return 返回结果
     */
    public Long getUserId() { return this.userId; }
    /**
     * set User Id
     * @param userId userId
     */
    public void setUserId(Long userId) { this.userId = userId; }
    /**
     * 获取Role
     * @return 返回结果
     */
    public String getRole() { return this.role; }
    /**
     * set Role
     * @param role role
     */
    public void setRole(String role) { this.role = role; }
}
