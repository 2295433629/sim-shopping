package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@TableName("t_banner")
public class BannerDO extends BaseEntity {
    private String status;
    private String imageUrl;
    private Integer sortOrder;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String title;
    private String linkUrl;

    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }
    public String getImageUrl() { return this.imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public Integer getSortOrder() { return this.sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public LocalDateTime getStartTime() { return this.startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return this.endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public String getTitle() { return this.title; }
    public void setTitle(String title) { this.title = title; }
    public String getLinkUrl() { return this.linkUrl; }
    public void setLinkUrl(String linkUrl) { this.linkUrl = linkUrl; }
}
