package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@TableName("t_sys_admin")
public class SysAdminDO extends BaseEntity {
    private String adminName;
    private Long userId;
    private String role;

    public String getAdminName() { return this.adminName; }
    public void setAdminName(String adminName) { this.adminName = adminName; }
    public Long getUserId() { return this.userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getRole() { return this.role; }
    public void setRole(String role) { this.role = role; }
}
