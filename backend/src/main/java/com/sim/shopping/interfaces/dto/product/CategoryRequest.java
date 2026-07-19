package com.sim.shopping.interfaces.dto.product;

import jakarta.validation.constraints.NotBlank;

/**
 * 分类请求DTO，封装接口入参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class CategoryRequest {

    @NotBlank(message = "分类名称不能为空")
    private String name;
    private Long parentId;
    private Integer sortOrder;
    private String icon;
    private String status;

    /** 获取Name */
    public String getName() { return this.name; }
    /** set Name */
    public void setName(String name) { this.name = name; }
    /** 获取Parent Id */
    public Long getParentId() { return this.parentId; }
    /** set Parent Id */
    public void setParentId(Long parentId) { this.parentId = parentId; }
    /** 获取Sort Order */
    public Integer getSortOrder() { return this.sortOrder; }
    /** set Sort Order */
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    /** 获取Icon */
    public String getIcon() { return this.icon; }
    /** set Icon */
    public void setIcon(String icon) { this.icon = icon; }
    /** 获取Status */
    public String getStatus() { return this.status; }
    /** set Status */
    public void setStatus(String status) { this.status = status; }
}
