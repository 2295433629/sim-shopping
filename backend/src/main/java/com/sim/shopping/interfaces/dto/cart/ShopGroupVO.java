package com.sim.shopping.interfaces.dto.cart;

import java.util.List;

/**
 * ShopGroup视图对象，用于前端展示
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class ShopGroupVO {
    private Long shopId;
    private String shopName;
    private List<CartItemVO> items;

    /**
     * 获取Shop Id
     * @return 返回结果
     */
    public Long getShopId() { return this.shopId; }
    /**
     * set Shop Id
     * @param shopId shopId
     */
    public void setShopId(Long shopId) { this.shopId = shopId; }
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
     * 获取Items
     * @return 返回结果
     */
    public List<CartItemVO> getItems() { return this.items; }
    /**
     * set Items
     * @param items items
     */
    public void setItems(List<CartItemVO> items) { this.items = items; }
}
