package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public String getPermission() { return this.permission; }
    public void setPermission(String permission) { this.permission = permission; }
    public String getPath() { return this.path; }
    public void setPath(String path) { this.path = path; }
    public Long getParentId() { return this.parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public Integer getVisible() { return this.visible; }
    public void setVisible(Integer visible) { this.visible = visible; }
    public String getType() { return this.type; }
    public void setType(String type) { this.type = type; }
    public String getIcon() { return this.icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }
    public String getComponent() { return this.component; }
    public void setComponent(String component) { this.component = component; }
    public Integer getSortOrder() { return this.sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
}
