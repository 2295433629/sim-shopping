package com.sim.shopping.interfaces.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AddressRequest {

    @NotBlank(message = "收货人姓名不能为空")
    @Size(max = 50, message = "收货人姓名长度不能超过50")
    private String receiverName;

    @NotBlank(message = "收货人电话不能为空")
    @Size(max = 20, message = "收货人电话长度不能超过20")
    private String receiverPhone;

    @NotBlank(message = "省份不能为空")
    private String province;

    @NotBlank(message = "城市不能为空")
    private String city;

    @NotBlank(message = "区/县不能为空")
    private String district;

    @NotBlank(message = "详细地址不能为空")
    @Size(max = 200, message = "详细地址长度不能超过200")
    private String detailAddress;

    private Integer isDefault;

    public String getReceiverName() { return this.receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }
    public String getReceiverPhone() { return this.receiverPhone; }
    public void setReceiverPhone(String receiverPhone) { this.receiverPhone = receiverPhone; }
    public String getProvince() { return this.province; }
    public void setProvince(String province) { this.province = province; }
    public String getCity() { return this.city; }
    public void setCity(String city) { this.city = city; }
    public String getDistrict() { return this.district; }
    public void setDistrict(String district) { this.district = district; }
    public String getDetailAddress() { return this.detailAddress; }
    public void setDetailAddress(String detailAddress) { this.detailAddress = detailAddress; }
    public Integer getIsDefault() { return this.isDefault; }
    public void setIsDefault(Integer isDefault) { this.isDefault = isDefault; }
}
