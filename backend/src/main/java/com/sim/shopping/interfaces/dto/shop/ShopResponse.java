package com.sim.shopping.interfaces.dto.shop;

import java.time.LocalDateTime;

public class ShopResponse {

    private Long shopId;
    private Long merchantId;
    private String shopName;
    private String shopLogo;
    private String description;
    private Integer productCount;
    private LocalDateTime createdAt;

    public Long getShopId() { return this.shopId; }
    public void setShopId(Long shopId) { this.shopId = shopId; }
    public Long getMerchantId() { return this.merchantId; }
    public void setMerchantId(Long merchantId) { this.merchantId = merchantId; }
    public String getShopName() { return this.shopName; }
    public void setShopName(String shopName) { this.shopName = shopName; }
    public String getShopLogo() { return this.shopLogo; }
    public void setShopLogo(String shopLogo) { this.shopLogo = shopLogo; }
    public String getDescription() { return this.description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getProductCount() { return this.productCount; }
    public void setProductCount(Integer productCount) { this.productCount = productCount; }
    public LocalDateTime getCreatedAt() { return this.createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
