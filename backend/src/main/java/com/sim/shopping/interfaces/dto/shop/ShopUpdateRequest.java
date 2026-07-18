package com.sim.shopping.interfaces.dto.shop;

import jakarta.validation.constraints.Size;

/**
 * ShopUpdate请求对象，封装接口入参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class ShopUpdateRequest {

    @Size(max = 100, message = "店铺名称长度不能超过100")
    private String shopName;

    @Size(max = 500, message = "店铺Logo URL长度不能超过500")
    private String shopLogo;

    @Size(max = 500, message = "店铺描述长度不能超过500")
    private String description;

    @Size(max = 50, message = "发货人姓名长度不能超过50")
    private String senderName;

    @Size(max = 20, message = "发货人电话长度不能超过20")
    private String senderPhone;

    @Size(max = 30, message = "发货省份长度不能超过30")
    private String senderProvince;

    @Size(max = 30, message = "发货城市长度不能超过30")
    private String senderCity;

    @Size(max = 30, message = "发货区县长度不能超过30")
    private String senderDistrict;

    @Size(max = 200, message = "发货详细地址长度不能超过200")
    private String senderAddress;

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
