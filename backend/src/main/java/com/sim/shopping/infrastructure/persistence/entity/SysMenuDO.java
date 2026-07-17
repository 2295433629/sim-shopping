package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * SysMenu数据实体，对应数据库表
 *
 * @author Sim Team
 * @since 1.0.0
 */
@TableName("t_sys_menu")
public class SysMenuDO extends BaseEntity {
    private String permission;
    private String path;
    private Long parentId;
    private Integer visible;
    private String type;
    private String icon;
    private String name;
    private String component;
    private Integer sortOrder;

    /**
     * 获取Permission
     * @return 返回结果
     */
    public String getPermission() { return this.permission; }
    /**
     * set Permission
     * @param permission permission
     */
    public void setPermission(String permission) { this.permission = permission; }
    /**
     * 获取Path
     * @return 返回结果
     */
    public String getPath() { return this.path; }
    /**
     * set Path
     * @param path path
     */
    public void setPath(String path) { this.path = path; }
    /**
     * 获取Parent Id
     * @return 返回结果
     */
    public Long getParentId() { return this.parentId; }
    /**
     * set Parent Id
     * @param parentId parentId
     */
    public void setParentId(Long parentId) { this.parentId = parentId; }
    /**
     * 获取Visible
     * @return 返回结果
     */
    public Integer getVisible() { return this.visible; }
    /**
     * set Visible
     * @param visible visible
     */
    public void setVisible(Integer visible) { this.visible = visible; }
    /** 获取Type */
    public String getType() { return this.type; }
    /** set Type */
    public void setType(String type) { this.type = type; }
    /**
     * 获取Icon
     * @return 返回结果
     */
    public String getIcon() { return this.icon; }
    /**
     * set Icon
     * @param icon icon
     */
    public void setIcon(String icon) { this.icon = icon; }
    /** 获取Name */
    public String getName() { return this.name; }
    /** set Name */
    public void setName(String name) { this.name = name; }
    /**
     * 获取Component
     * @return 返回结果
     */
    public String getComponent() { return this.component; }
    /**
     * set Component
     * @param component component
     */
    public void setComponent(String component) { this.component = component; }
    /**
     * 获取Sort Order
     * @return 返回结果
     */
    public Integer getSortOrder() { return this.sortOrder; }
    /**
     * set Sort Order
     * @param sortOrder sortOrder
     */
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
}
