package com.sim.shopping.infrastructure.persistence.entity;

import java.time.LocalDateTime;

/**
 * 活动查询结果，用于 Mapper 自定义 SQL 返回，包含活动基本信息和商品数量
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class ActivityQueryResult {

    private Long id;
    private String activityName;
    private String bannerImage;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer sortOrder;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long productCount;

    /** 获取Id */
    public Long getId() { return this.id; }
    /** set Id */
    public void setId(Long id) { this.id = id; }
    /** 获取Activity Name */
    public String getActivityName() { return this.activityName; }
    /** set Activity Name */
    public void setActivityName(String activityName) { this.activityName = activityName; }
    /** 获取Banner Image */
    public String getBannerImage() { return this.bannerImage; }
    /** set Banner Image */
    public void setBannerImage(String bannerImage) { this.bannerImage = bannerImage; }
    /** 获取Description */
    public String getDescription() { return this.description; }
    /** set Description */
    public void setDescription(String description) { this.description = description; }
    /** 获取Start Time */
    public LocalDateTime getStartTime() { return this.startTime; }
    /** set Start Time */
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    /** 获取End Time */
    public LocalDateTime getEndTime() { return this.endTime; }
    /** set End Time */
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    /** 获取Sort Order */
    public Integer getSortOrder() { return this.sortOrder; }
    /** set Sort Order */
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
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
    /** 获取Product Count */
    public Long getProductCount() { return this.productCount; }
    /** set Product Count */
    public void setProductCount(Long productCount) { this.productCount = productCount; }
}
