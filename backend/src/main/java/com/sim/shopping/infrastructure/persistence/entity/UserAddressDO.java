package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * 地址实体，对应 t_user_address 表，存储收货地址
 *
 * @author Sim Team
 * @since 1.0.0
 */
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
     * 获取Is Default
     * @return 返回结果
     */
    public Integer getIsDefault() { return this.isDefault; }
    /**
     * set Is Default
     * @param isDefault isDefault
     */
    public void setIsDefault(Integer isDefault) { this.isDefault = isDefault; }
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
}
