package com.sim.shopping.interfaces.dto.merchant;

import java.time.LocalDateTime;

/**
 * MerchantList响应对象，封装接口出参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class MerchantListResponse {

    private Long merchantId;
    private Long userId;
    private String merchantName;
    private String contactPhone;
    private String status;
    private LocalDateTime createdAt;

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
     * 获取User Id
     * @return 返回结果
     */
    public Long getUserId() { return this.userId; }
    /**
     * set User Id
     * @param userId userId
     */
    public void setUserId(Long userId) { this.userId = userId; }
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
     * 获取Status
     * @return 返回结果
     */
    public String getStatus() { return this.status; }
    /**
     * set Status
     * @param status status
     */
    public void setStatus(String status) { this.status = status; }
    /** 获取Created At */
    public LocalDateTime getCreatedAt() { return this.createdAt; }
    /** set Created At */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
