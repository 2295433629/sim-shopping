package com.sim.shopping.interfaces.dto.points;

import java.time.LocalDateTime;

/**
 * PointsProduct响应对象，封装接口出参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class PointsProductResponse {

    private Long id;
    private String productName;
    private String description;
    private String imageUrl;
    private Integer pointsRequired;
    private Integer stock;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** 获取Id */
    public Long getId() {
        return id;
    }

    /** set Id */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取Product Name
     * @return 返回结果
     */
    public String getProductName() {
        return productName;
    }

    /**
     * set Product Name
     * @param productName productName
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * 获取Description
     * @return 返回结果
     */
    public String getDescription() {
        return description;
    }

    /**
     * set Description
     * @param description description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取Image Url
     * @return 返回结果
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * set Image Url
     * @param imageUrl imageUrl
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * 获取Points Required
     * @return 返回结果
     */
    public Integer getPointsRequired() {
        return pointsRequired;
    }

    /**
     * set Points Required
     * @param pointsRequired pointsRequired
     */
    public void setPointsRequired(Integer pointsRequired) {
        this.pointsRequired = pointsRequired;
    }

    /**
     * 获取Stock
     * @return 返回结果
     */
    public Integer getStock() {
        return stock;
    }

    /**
     * set Stock
     * @param stock stock
     */
    public void setStock(Integer stock) {
        this.stock = stock;
    }

    /**
     * 获取Status
     * @return 返回结果
     */
    public String getStatus() {
        return status;
    }

    /**
     * set Status
     * @param status status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /** 获取Created At */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /** set Created At */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /** 获取Updated At */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /** set Updated At */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
