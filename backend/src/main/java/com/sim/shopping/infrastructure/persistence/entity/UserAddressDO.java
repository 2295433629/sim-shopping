package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@TableName("t_user_address")
public class UserAddressDO extends BaseEntity {
    private String detailAddress;
    private String district;
    private Long userId;
    private String city;
    private String province;
    private String receiverPhone;
    private Integer isDefault;
    private String receiverName;

    public String getDetailAddress() { return this.detailAddress; }
    public void setDetailAddress(String detailAddress) { this.detailAddress = detailAddress; }
    public String getDistrict() { return this.district; }
    public void setDistrict(String district) { this.district = district; }
    public Long getUserId() { return this.userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getCity() { return this.city; }
    public void setCity(String city) { this.city = city; }
    public String getProvince() { return this.province; }
    public void setProvince(String province) { this.province = province; }
    public String getReceiverPhone() { return this.receiverPhone; }
    public void setReceiverPhone(String receiverPhone) { this.receiverPhone = receiverPhone; }
    public Integer getIsDefault() { return this.isDefault; }
    public void setIsDefault(Integer isDefault) { this.isDefault = isDefault; }
    public String getReceiverName() { return this.receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }
}
