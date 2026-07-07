package com.sim.shopping.interfaces.dto.banner;

import java.time.LocalDateTime;

public class BannerResponse {
    private Long id;
    private String title;
    private String imageUrl;
    private String linkUrl;
    private Integer sortOrder;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;

    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return this.title; }
    public void setTitle(String title) { this.title = title; }
    public String getImageUrl() { return this.imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getLinkUrl() { return this.linkUrl; }
    public void setLinkUrl(String linkUrl) { this.linkUrl = linkUrl; }
    public Integer getSortOrder() { return this.sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public LocalDateTime getStartTime() { return this.startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return this.endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }
}
