package com.sim.shopping.interfaces.dto.activity;

import java.time.LocalDateTime;
import java.util.List;

public class ActivityDetailResponse {

    private Long id;
    private String activityName;
    private String bannerImage;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer sortOrder;
    private String status;
    private Long productCount;
    private List<ActivityProductResponse> products;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActivityName() {
        return this.activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getBannerImage() {
        return this.bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Integer getSortOrder() {
        return this.sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getProductCount() {
        return this.productCount;
    }

    public void setProductCount(Long productCount) {
        this.productCount = productCount;
    }

    public List<ActivityProductResponse> getProducts() {
        return this.products;
    }

    public void setProducts(List<ActivityProductResponse> products) {
        this.products = products;
    }
}
