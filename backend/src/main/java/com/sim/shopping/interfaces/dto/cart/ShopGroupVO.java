package com.sim.shopping.interfaces.dto.cart;

import java.util.List;

public class ShopGroupVO {
    private Long shopId;
    private String shopName;
    private List<CartItemVO> items;

    public Long getShopId() { return this.shopId; }
    public void setShopId(Long shopId) { this.shopId = shopId; }
    public String getShopName() { return this.shopName; }
    public void setShopName(String shopName) { this.shopName = shopName; }
    public List<CartItemVO> getItems() { return this.items; }
    public void setItems(List<CartItemVO> items) { this.items = items; }
}
