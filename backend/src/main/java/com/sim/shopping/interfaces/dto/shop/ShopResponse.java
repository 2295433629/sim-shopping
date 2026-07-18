package com.sim.shopping.interfaces.dto.shop;

import java.time.LocalDateTime;

/**
 * Shop响应对象，封装接口出参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class ShopResponse {

    private Long shopId;
    private Long merchantId;
    private String shopName;
    private String shopLogo;
    private String description;
    private Integer productCount;
    private LocalDateTime createdAt;
    private String senderName;
    private String senderPhone;
    private String senderProvince;
    private String senderCity;
    private String senderDistrict;
    private String senderAddress;

    /**
     * 获取Shop Id
     * @return 返回结果
     */
    public Long getShopId() { return this.shopId; }
    /**
     * set Shop Id
     * @param shopId shopId
     */
    public void setShopId(Long shopId) { this.shopId = shopId; }
    /**
     * 获取Merchant Id
     * @return 返回结果
     */
    public Long getMerchantId() { return this.merchantId; }
    /**
     * set Merchant Id
     * @param merchantId merchantId
     */
    public void setMerchantId(Long merchantId) { this.merchantId = merchantId; }
    /**
     * 获取Shop Name
     * @return 返回结果
     */
    public String getShopName() { return this.shopName; }
    /**
     * set Shop Name
     * @param shopName shopName
     */
    public void setShopName(String shopName) { this.shopName = shopName; }
    /**
     * 获取Shop Logo
     * @return 返回结果
     */
    public String getShopLogo() { return this.shopLogo; }
    /**
     * set Shop Logo
     * @param shopLogo shopLogo
     */
    public void setShopLogo(String shopLogo) { this.shopLogo = shopLogo; }
    /**
     * 获取Description
     * @return 返回结果
     */
    public String getDescription() { return this.description; }
    /**
     * set Description
     * @param description description
     */
    public void setDescription(String description) { this.description = description; }
    /**
     * 获取Product Count
     * @return 返回结果
     */
    public Integer getProductCount() { return this.productCount; }
    /**
     * set Product Count
     * @param productCount productCount
     */
    public void setProductCount(Integer productCount) { this.productCount = productCount; }
    /** 获取Created At */
    public LocalDateTime getCreatedAt() { return this.createdAt; }
    /** set Created At */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    /** 获取Sender Name */
    public String getSenderName() { return this.senderName; }
    /** set Sender Name */
    public void setSenderName(String senderName) { this.senderName = senderName; }
    /** 获取Sender Phone */
    public String getSenderPhone() { return this.senderPhone; }
    /** set Sender Phone */
    public void setSenderPhone(String senderPhone) { this.senderPhone = senderPhone; }
    /** 获取Sender Province */
    public String getSenderProvince() { return this.senderProvince; }
    /** set Sender Province */
    public void setSenderProvince(String senderProvince) { this.senderProvince = senderProvince; }
    /** 获取Sender City */
    public String getSenderCity() { return this.senderCity; }
    /** set Sender City */
    public void setSenderCity(String senderCity) { this.senderCity = senderCity; }
    /** 获取Sender District */
    public String getSenderDistrict() { return this.senderDistrict; }
    /** set Sender District */
    public void setSenderDistrict(String senderDistrict) { this.senderDistrict = senderDistrict; }
    /** 获取Sender Address */
    public String getSenderAddress() { return this.senderAddress; }
    /** set Sender Address */
    public void setSenderAddress(String senderAddress) { this.senderAddress = senderAddress; }
}
