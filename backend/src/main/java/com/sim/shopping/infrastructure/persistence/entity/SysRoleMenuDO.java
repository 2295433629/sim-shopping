package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@TableName("t_sys_role_menu")
public class SysRoleMenuDO extends BaseEntity {
    private Long roleId;
    private Long menuId;

    public Long getRoleId() { return this.roleId; }
    public void setRoleId(Long roleId) { this.roleId = roleId; }
    public Long getMenuId() { return this.menuId; }
    public void setMenuId(Long menuId) { this.menuId = menuId; }
}
