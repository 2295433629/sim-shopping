package com.sim.shopping.interfaces.dto.product;

import java.time.LocalDateTime;

/**
 * 品牌响应DTO，封装接口出参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class BrandResponse {

    private Long id;
    private String brandName;
    private String brandLogo;
    private String brandDescription;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** 获取Id */
    public Long getId() { return this.id; }
    /** set Id */
    public void setId(Long id) { this.id = id; }
    /** 获取Brand Name */
    public String getBrandName() { return this.brandName; }
    /** set Brand Name */
    public void setBrandName(String brandName) { this.brandName = brandName; }
    /** 获取Brand Logo */
    public String getBrandLogo() { return this.brandLogo; }
    /** set Brand Logo */
    public void setBrandLogo(String brandLogo) { this.brandLogo = brandLogo; }
    /** 获取Brand Description */
    public String getBrandDescription() { return this.brandDescription; }
    /** set Brand Description */
    public void setBrandDescription(String brandDescription) { this.brandDescription = brandDescription; }
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
