package com.sim.shopping.interfaces.dto.system;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Menu请求对象，封装接口入参
 *
 * @author Sim Team
 * @since 1.0.0
 */
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
}
