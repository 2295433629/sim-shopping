package com.sim.shopping.interfaces.dto.product;

import java.time.LocalDateTime;

/**
 * 分类响应DTO，封装接口出参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class CategoryResponse {

    private Long id;
    private Long parentId;
    private String name;
    private Integer level;
    private Integer sortOrder;
    private String icon;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** 获取Id */
    public Long getId() { return this.id; }
    /** set Id */
    public void setId(Long id) { this.id = id; }
    /** 获取Parent Id */
    public Long getParentId() { return this.parentId; }
    /** set Parent Id */
    public void setParentId(Long parentId) { this.parentId = parentId; }
    /** 获取Name */
    public String getName() { return this.name; }
    /** set Name */
    public void setName(String name) { this.name = name; }
    /** 获取Level */
    public Integer getLevel() { return this.level; }
    /** set Level */
    public void setLevel(Integer level) { this.level = level; }
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
    /** 获取Created At */
    public LocalDateTime getCreatedAt() { return this.createdAt; }
    /** set Created At */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    /** 获取Updated At */
    public LocalDateTime getUpdatedAt() { return this.updatedAt; }
    /** set Updated At */
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
