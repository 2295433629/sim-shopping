package com.sim.shopping.interfaces.dto.merchant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

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

    public String getMerchantName() { return this.merchantName; }
    public void setMerchantName(String merchantName) { this.merchantName = merchantName; }
    public String getContactPhone() { return this.contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public String getContactEmail() { return this.contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    public String getBusinessLicense() { return this.businessLicense; }
    public void setBusinessLicense(String businessLicense) { this.businessLicense = businessLicense; }
}
