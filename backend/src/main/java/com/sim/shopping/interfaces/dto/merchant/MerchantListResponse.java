package com.sim.shopping.interfaces.dto.merchant;

import java.time.LocalDateTime;

public class MerchantListResponse {

    private Long merchantId;
    private Long userId;
    private String merchantName;
    private String contactPhone;
    private String status;
    private LocalDateTime createdAt;

    public Long getMerchantId() { return this.merchantId; }
    public void setMerchantId(Long merchantId) { this.merchantId = merchantId; }
    public Long getUserId() { return this.userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getMerchantName() { return this.merchantName; }
    public void setMerchantName(String merchantName) { this.merchantName = merchantName; }
    public String getContactPhone() { return this.contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return this.createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
