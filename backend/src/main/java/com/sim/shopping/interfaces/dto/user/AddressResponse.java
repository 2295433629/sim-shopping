package com.sim.shopping.interfaces.dto.user;

import java.time.LocalDateTime;

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

    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }
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
    public LocalDateTime getCreatedAt() { return this.createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return this.updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
