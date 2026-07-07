package com.sim.shopping.interfaces.dto.system;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MenuRequest {

    private Long parentId;

    @NotBlank(message = "菜单名称不能为空")
    private String menuName;

    @NotBlank(message = "菜单类型不能为空")
    private String menuType;

    private String path;
    private String component;
    private String icon;
    private Integer sortOrder;
    private Integer visible;

    public Long getParentId() { return this.parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public String getMenuName() { return this.menuName; }
    public void setMenuName(String menuName) { this.menuName = menuName; }
    public String getMenuType() { return this.menuType; }
    public void setMenuType(String menuType) { this.menuType = menuType; }
    public String getPath() { return this.path; }
    public void setPath(String path) { this.path = path; }
    public String getComponent() { return this.component; }
    public void setComponent(String component) { this.component = component; }
    public String getIcon() { return this.icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public Integer getSortOrder() { return this.sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public Integer getVisible() { return this.visible; }
    public void setVisible(Integer visible) { this.visible = visible; }
}
