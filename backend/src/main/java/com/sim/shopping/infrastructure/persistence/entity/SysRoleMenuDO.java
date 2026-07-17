package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * SysRoleMenu数据实体，对应数据库表
 *
 * @author Sim Team
 * @since 1.0.0
 */
@TableName("t_sys_role_menu")
public class SysRoleMenuDO extends BaseEntity {
    private Long roleId;
    private Long menuId;

    /**
     * 获取Role Id
     * @return 返回结果
     */
    public Long getRoleId() { return this.roleId; }
    /**
     * set Role Id
     * @param roleId roleId
     */
    public void setRoleId(Long roleId) { this.roleId = roleId; }
    /**
     * 获取Menu Id
     * @return 返回结果
     */
    public Long getMenuId() { return this.menuId; }
    /**
     * set Menu Id
     * @param menuId menuId
     */
    public void setMenuId(Long menuId) { this.menuId = menuId; }
}
