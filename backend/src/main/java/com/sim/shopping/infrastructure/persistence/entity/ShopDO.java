package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
/**
 * 店铺实体，对应 t_shop 表，存储店铺信息
 *
 * @author Sim Team
 * @since 1.0.0
 */
@TableName("t_shop")
public class ShopDO extends BaseEntity {
    private String status;
    private String shopLogo;
    private Long merchantId;
    private String shopName;
    private String description;

    @TableField("balance")
    private BigDecimal balance;

    @TableField("total_income")
    private BigDecimal totalIncome;

    @TableField("total_settled")
    private BigDecimal totalSettled;

    @TableField("frozen_amount")
    private BigDecimal frozenAmount;

    @TableField("sender_name")
    private String senderName;

    @TableField("sender_phone")
    private String senderPhone;

    @TableField("sender_province")
    private String senderProvince;

    @TableField("sender_city")
    private String senderCity;

    @TableField("sender_district")
    private String senderDistrict;

    @TableField("sender_address")
    private String senderAddress;

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
     * 获取Shop Logo
     * @return 返回结果
     */
    public String getShopLogo() { return this.shopLogo; }
    /**
     * set Shop Logo
     * @param shopLogo shopLogo
     */
    public void setShopLogo(String shopLogo) { this.shopLogo = shopLogo; }
    /**
     * 获取Merchant Id
     * @return 返回结果
     */
    public Long getMerchantId() { return this.merchantId; }
    /**
     * set Merchant Id
     * @param merchantId merchantId
     */
    public void setMerchantId(Long merchantId) { this.merchantId = merchantId; }
    /**
     * 获取Shop Name
     * @return 返回结果
     */
    public String getShopName() { return this.shopName; }
    /**
     * set Shop Name
     * @param shopName shopName
     */
    public void setShopName(String shopName) { this.shopName = shopName; }
    /**
     * 获取Description
     * @return 返回结果
     */
    public String getDescription() { return this.description; }
    /**
     * set Description
     * @param description description
     */
    public void setDescription(String description) { this.description = description; }
    /**
     * 获取Balance
     * @return 返回结果
     */
    public BigDecimal getBalance() { return this.balance; }
    /**
     * set Balance
     * @param balance balance
     */
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    /**
     * 获取Total Income
     * @return 返回结果
     */
    public BigDecimal getTotalIncome() { return this.totalIncome; }
    /**
     * set Total Income
     * @param totalIncome totalIncome
     */
    public void setTotalIncome(BigDecimal totalIncome) { this.totalIncome = totalIncome; }
    /**
     * 获取Total Settled
     * @return 返回结果
     */
    public BigDecimal getTotalSettled() { return this.totalSettled; }
    /**
     * set Total Settled
     * @param totalSettled totalSettled
     */
    public void setTotalSettled(BigDecimal totalSettled) { this.totalSettled = totalSettled; }
    /**
     * 获取Frozen Amount
     * @return 返回结果
     */
    public BigDecimal getFrozenAmount() { return this.frozenAmount; }
    /**
     * set Frozen Amount
     * @param frozenAmount frozenAmount
     */
    public void setFrozenAmount(BigDecimal frozenAmount) { this.frozenAmount = frozenAmount; }
    /**
     * 获取Sender Name
     * @return 返回结果
     */
    public String getSenderName() { return this.senderName; }
    /**
     * set Sender Name
     * @param senderName senderName
     */
    public void setSenderName(String senderName) { this.senderName = senderName; }
    /**
     * 获取Sender Phone
     * @return 返回结果
     */
    public String getSenderPhone() { return this.senderPhone; }
    /**
     * set Sender Phone
     * @param senderPhone senderPhone
     */
    public void setSenderPhone(String senderPhone) { this.senderPhone = senderPhone; }
    /**
     * 获取Sender Province
     * @return 返回结果
     */
    public String getSenderProvince() { return this.senderProvince; }
    /**
     * set Sender Province
     * @param senderProvince senderProvince
     */
    public void setSenderProvince(String senderProvince) { this.senderProvince = senderProvince; }
    /**
     * 获取Sender City
     * @return 返回结果
     */
    public String getSenderCity() { return this.senderCity; }
    /**
     * set Sender City
     * @param senderCity senderCity
     */
    public void setSenderCity(String senderCity) { this.senderCity = senderCity; }
    /**
     * 获取Sender District
     * @return 返回结果
     */
    public String getSenderDistrict() { return this.senderDistrict; }
    /**
     * set Sender District
     * @param senderDistrict senderDistrict
     */
    public void setSenderDistrict(String senderDistrict) { this.senderDistrict = senderDistrict; }
    /**
     * 获取Sender Address
     * @return 返回结果
     */
    public String getSenderAddress() { return this.senderAddress; }
    /**
     * set Sender Address
     * @param senderAddress senderAddress
     */
    public void setSenderAddress(String senderAddress) { this.senderAddress = senderAddress; }
}
