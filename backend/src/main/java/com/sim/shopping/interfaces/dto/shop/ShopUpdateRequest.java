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
}
