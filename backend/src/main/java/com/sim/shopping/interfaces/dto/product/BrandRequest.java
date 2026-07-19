package com.sim.shopping.interfaces.dto.product;

import jakarta.validation.constraints.NotBlank;

/**
 * 品牌请求DTO，封装接口入参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class BrandRequest {

    @NotBlank(message = "品牌名称不能为空")
    private String brandName;
    private String brandLogo;
    private String brandDescription;
    private String status;

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
}
