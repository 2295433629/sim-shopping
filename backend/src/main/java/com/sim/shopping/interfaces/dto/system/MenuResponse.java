package com.sim.shopping.interfaces.dto.system;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MenuResponse {

    private Long id;
    private Long parentId;
    private String menuName;
    private String menuType;
    private String path;
    private String component;
    private String icon;
    private Integer sortOrder;
    private Integer visible;
    private LocalDateTime createdAt;
    private List<MenuResponse> children = new ArrayList<>();

    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }
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
    public LocalDateTime getCreatedAt() { return this.createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public List<MenuResponse> getChildren() { return this.children; }
    public void setChildren(List<MenuResponse> children) { this.children = children; }
}
