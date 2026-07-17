package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * Banner实体，对应 t_banner 表，存储首页轮播图
 *
 * @author Sim Team
 * @since 1.0.0
 */
@TableName("t_banner")
public class BannerDO extends BaseEntity {
    private String status;
    private String imageUrl;
    private Integer sortOrder;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String title;
    private String linkUrl;

    /**
     * 获取Status
     * @return 返回结果
     */
    public String getStatus() { return this.status; }
    /**
     * set Status
     * @param status status
     */
    public void setStatus(String status) { this.status = status; }
    /**
     * 获取Image Url
     * @return 返回结果
     */
    public String getImageUrl() { return this.imageUrl; }
    /**
     * set Image Url
     * @param imageUrl imageUrl
     */
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
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
     * 获取Start Time
     * @return 返回结果
     */
    public LocalDateTime getStartTime() { return this.startTime; }
    /**
     * set Start Time
     * @param startTime startTime
     */
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    /**
     * 获取End Time
     * @return 返回结果
     */
    public LocalDateTime getEndTime() { return this.endTime; }
    /**
     * set End Time
     * @param endTime endTime
     */
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    /**
     * 获取Title
     * @return 返回结果
     */
    public String getTitle() { return this.title; }
    /**
     * set Title
     * @param title title
     */
    public void setTitle(String title) { this.title = title; }
    /**
     * 获取Link Url
     * @return 返回结果
     */
    public String getLinkUrl() { return this.linkUrl; }
    /**
     * set Link Url
     * @param linkUrl linkUrl
     */
    public void setLinkUrl(String linkUrl) { this.linkUrl = linkUrl; }
}
