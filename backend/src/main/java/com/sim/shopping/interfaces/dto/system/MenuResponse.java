package com.sim.shopping.interfaces.dto.system;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Menu响应对象，封装接口出参
 *
 * @author Sim Team
 * @since 1.0.0
 */
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

    /** 获取Id */
    public Long getId() { return this.id; }
    /** set Id */
    public void setId(Long id) { this.id = id; }
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
     * 获取Menu Name
     * @return 返回结果
     */
    public String getMenuName() { return this.menuName; }
    /**
     * set Menu Name
     * @param menuName menuName
     */
    public void setMenuName(String menuName) { this.menuName = menuName; }
    /**
     * 获取Menu Type
     * @return 返回结果
     */
    public String getMenuType() { return this.menuType; }
    /**
     * set Menu Type
     * @param menuType menuType
     */
    public void setMenuType(String menuType) { this.menuType = menuType; }
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
     * 获取Icon
     * @return 返回结果
     */
    public String getIcon() { return this.icon; }
    /**
     * set Icon
     * @param icon icon
     */
    public void setIcon(String icon) { this.icon = icon; }
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
    /** 获取Created At */
    public LocalDateTime getCreatedAt() { return this.createdAt; }
    /** set Created At */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    /**
     * 获取Children
     * @return 返回结果
     */
    public List<MenuResponse> getChildren() { return this.children; }
    /**
     * set Children
     * @param children children
     */
    public void setChildren(List<MenuResponse> children) { this.children = children; }
}
