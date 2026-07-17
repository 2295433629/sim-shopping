package com.sim.shopping.interfaces.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Address请求对象，封装接口入参
 *
 * @author Sim Team
 * @since 1.0.0
 */
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

    /**
     * 获取Receiver Name
     * @return 返回结果
     */
    public String getReceiverName() { return this.receiverName; }
    /**
     * set Receiver Name
     * @param receiverName receiverName
     */
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }
    /**
     * 获取Receiver Phone
     * @return 返回结果
     */
    public String getReceiverPhone() { return this.receiverPhone; }
    /**
     * set Receiver Phone
     * @param receiverPhone receiverPhone
     */
    public void setReceiverPhone(String receiverPhone) { this.receiverPhone = receiverPhone; }
    /**
     * 获取Province
     * @return 返回结果
     */
    public String getProvince() { return this.province; }
    /**
     * set Province
     * @param province province
     */
    public void setProvince(String province) { this.province = province; }
    /**
     * 获取City
     * @return 返回结果
     */
    public String getCity() { return this.city; }
    /**
     * set City
     * @param city city
     */
    public void setCity(String city) { this.city = city; }
    /**
     * 获取District
     * @return 返回结果
     */
    public String getDistrict() { return this.district; }
    /**
     * set District
     * @param district district
     */
    public void setDistrict(String district) { this.district = district; }
    /**
     * 获取Detail Address
     * @return 返回结果
     */
    public String getDetailAddress() { return this.detailAddress; }
    /**
     * set Detail Address
     * @param detailAddress detailAddress
     */
    public void setDetailAddress(String detailAddress) { this.detailAddress = detailAddress; }
    /**
     * 获取Is Default
     * @return 返回结果
     */
    public Integer getIsDefault() { return this.isDefault; }
    /**
     * set Is Default
     * @param isDefault isDefault
     */
    public void setIsDefault(Integer isDefault) { this.isDefault = isDefault; }
}
