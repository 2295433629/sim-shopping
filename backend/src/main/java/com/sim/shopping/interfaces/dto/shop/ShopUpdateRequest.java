package com.sim.shopping.interfaces.dto.shop;

import jakarta.validation.constraints.Size;

public class ShopUpdateRequest {

    @Size(max = 100, message = "店铺名称长度不能超过100")
    private String shopName;

    @Size(max = 500, message = "店铺Logo URL长度不能超过500")
    private String shopLogo;

    @Size(max = 500, message = "店铺描述长度不能超过500")
    private String description;

    public String getShopName() { return this.shopName; }
    public void setShopName(String shopName) { this.shopName = shopName; }
    public String getShopLogo() { return this.shopLogo; }
    public void setShopLogo(String shopLogo) { this.shopLogo = shopLogo; }
    public String getDescription() { return this.description; }
    public void setDescription(String description) { this.description = description; }
}
