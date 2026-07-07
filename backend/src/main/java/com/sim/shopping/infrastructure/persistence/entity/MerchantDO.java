package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@TableName("t_merchant")
public class MerchantDO extends BaseEntity {
    private String merchantName;
    private String contactPhone;
    private Long userId;
    private String status;
    private LocalDateTime approvedAt;
    private String contactEmail;
    private String businessLicense;
    private Long approvedBy;

    public String getMerchantName() { return this.merchantName; }
    public void setMerchantName(String merchantName) { this.merchantName = merchantName; }
    public String getContactPhone() { return this.contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public Long getUserId() { return this.userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getApprovedAt() { return this.approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }
    public String getContactEmail() { return this.contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    public String getBusinessLicense() { return this.businessLicense; }
    public void setBusinessLicense(String businessLicense) { this.businessLicense = businessLicense; }
    public Long getApprovedBy() { return this.approvedBy; }
    public void setApprovedBy(Long approvedBy) { this.approvedBy = approvedBy; }
}
