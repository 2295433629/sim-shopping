package com.sim.shopping.interfaces.dto.merchant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * MerchantApply请求对象，封装接口入参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class MerchantApplyRequest {

    @NotBlank(message = "商家名称不能为空")
    @Size(max = 100, message = "商家名称长度不能超过100")
    private String merchantName;

    @NotBlank(message = "联系电话不能为空")
    @Size(max = 20, message = "联系电话长度不能超过20")
    private String contactPhone;

    @Size(max = 100, message = "联系邮箱长度不能超过100")
    private String contactEmail;

    @Size(max = 50, message = "营业执照号长度不能超过50")
    private String businessLicense;

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
     * 获取Business License
     * @return 返回结果
     */
    public String getBusinessLicense() { return this.businessLicense; }
    /**
     * set Business License
     * @param businessLicense businessLicense
     */
    public void setBusinessLicense(String businessLicense) { this.businessLicense = businessLicense; }
}
