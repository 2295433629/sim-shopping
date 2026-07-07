package com.sim.shopping.interfaces.dto.merchant;

public class MerchantInfoResponse {

    private Long merchantId;
    private String merchantName;
    private String contactPhone;
    private String contactEmail;
    private String status;
    private Long shopId;
    private String shopName;

    public Long getMerchantId() { return this.merchantId; }
    public void setMerchantId(Long merchantId) { this.merchantId = merchantId; }
    public String getMerchantName() { return this.merchantName; }
    public void setMerchantName(String merchantName) { this.merchantName = merchantName; }
    public String getContactPhone() { return this.contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public String getContactEmail() { return this.contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }
    public Long getShopId() { return this.shopId; }
    public void setShopId(Long shopId) { this.shopId = shopId; }
    public String getShopName() { return this.shopName; }
    public void setShopName(String shopName) { this.shopName = shopName; }
}
