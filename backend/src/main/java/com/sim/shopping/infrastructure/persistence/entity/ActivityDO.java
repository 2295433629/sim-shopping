package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * 专题活动实体，对应 t_activity 表，存储专题活动信息
 *
 * @author Sim Team
 * @since 1.0.0
 */
@TableName("t_activity")
public class ActivityDO extends BaseEntity {

    private String activityName;
    private String bannerImage;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer sortOrder;
    private String status;

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
}
