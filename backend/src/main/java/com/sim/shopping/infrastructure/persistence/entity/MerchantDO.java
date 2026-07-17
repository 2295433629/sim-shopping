package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * Merchant数据实体，对应数据库表
 *
 * @author Sim Team
 * @since 1.0.0
 */
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
     * 获取Approved At
     * @return 返回结果
     */
    public LocalDateTime getApprovedAt() { return this.approvedAt; }
    /**
     * set Approved At
     * @param approvedAt approvedAt
     */
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }
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
     * 获取Approved By
     * @return 返回结果
     */
    public Long getApprovedBy() { return this.approvedBy; }
    /**
     * set Approved By
     * @param approvedBy approvedBy
     */
    public void setApprovedBy(Long approvedBy) { this.approvedBy = approvedBy; }
}
