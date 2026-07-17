package com.sim.shopping.interfaces.dto.user;

import java.time.LocalDateTime;

/**
 * Address响应对象，封装接口出参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class AddressResponse {

    private Long id;
    private String receiverName;
    private String receiverPhone;
    private String province;
    private String city;
    private String district;
    private String detailAddress;
    private Integer isDefault;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** 获取Id */
    public Long getId() { return this.id; }
    /** set Id */
    public void setId(Long id) { this.id = id; }
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
    /** 获取Created At */
    public LocalDateTime getCreatedAt() { return this.createdAt; }
    /** set Created At */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    /** 获取Updated At */
    public LocalDateTime getUpdatedAt() { return this.updatedAt; }
    /** set Updated At */
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
