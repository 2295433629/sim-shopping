package com.sim.shopping.interfaces.dto.merchant;

/**
 * MerchantInfo响应对象，封装接口出参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class MerchantInfoResponse {

    private Long merchantId;
    private String merchantName;
    private String contactPhone;
    private String contactEmail;
    private String status;
    private Long shopId;
    private String shopName;
    private String businessLicense;
    private String rejectReason;

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
     * 获取Merchant Name
     * @return 返回结果
     */
    public String getMerchantName() { return this.merchantName; }
    /**
     * set Merchant Name
     * @param merchantName merchantName
     */
    public void setMerchantName(String merchantName) { this.merchantName = merchantName; }
    /**
     * 获取Contact Phone
     * @return 返回结果
     */
    public String getContactPhone() { return this.contactPhone; }
    /**
     * set Contact Phone
     * @param contactPhone contactPhone
     */
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    /**
     * 获取Contact Email
     * @return 返回结果
     */
    public String getContactEmail() { return this.contactEmail; }
    /**
     * set Contact Email
     * @param contactEmail contactEmail
     */
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
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
     * 获取Business License
     * @return 返回结果
     */
    public String getBusinessLicense() { return this.businessLicense; }
    /**
     * set Business License
     * @param businessLicense businessLicense
     */
    public void setBusinessLicense(String businessLicense) { this.businessLicense = businessLicense; }
    /**
     * 获取Reject Reason
     * @return 返回结果
     */
    public String getRejectReason() { return this.rejectReason; }
    /**
     * set Reject Reason
     * @param rejectReason rejectReason
     */
    public void setRejectReason(String rejectReason) { this.rejectReason = rejectReason; }
}
