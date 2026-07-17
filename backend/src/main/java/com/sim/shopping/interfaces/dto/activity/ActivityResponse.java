package com.sim.shopping.interfaces.dto.activity;

import java.time.LocalDateTime;

/**
 * 活动响应DTO，包含专题活动基本信息
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class ActivityResponse {

    private Long id;
    private String activityName;
    private String bannerImage;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer sortOrder;
    private String status;
    private Long productCount;

    /** 获取Id */
    public Long getId() {
        return this.id;
    }

    /** set Id */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取Activity Name
     * @return 返回结果
     */
    public String getActivityName() {
        return this.activityName;
    }

    /**
     * set Activity Name
     * @param activityName activityName
     */
    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    /**
     * 获取Banner Image
     * @return 返回结果
     */
    public String getBannerImage() {
        return this.bannerImage;
    }

    /**
     * set Banner Image
     * @param bannerImage bannerImage
     */
    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    /**
     * 获取Description
     * @return 返回结果
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * set Description
     * @param description description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取Start Time
     * @return 返回结果
     */
    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    /**
     * set Start Time
     * @param startTime startTime
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取End Time
     * @return 返回结果
     */
    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    /**
     * set End Time
     * @param endTime endTime
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取Sort Order
     * @return 返回结果
     */
    public Integer getSortOrder() {
        return this.sortOrder;
    }

    /**
     * set Sort Order
     * @param sortOrder sortOrder
     */
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * 获取Status
     * @return 返回结果
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * set Status
     * @param status status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取Product Count
     * @return 返回结果
     */
    public Long getProductCount() {
        return this.productCount;
    }

    /**
     * set Product Count
     * @param productCount productCount
     */
    public void setProductCount(Long productCount) {
        this.productCount = productCount;
    }
}
