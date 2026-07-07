package com.sim.shopping.interfaces.dto.cart;

import java.math.BigDecimal;
import java.util.List;

public class CartResponse {
    private Long cartId;
    private List<ShopGroupVO> shopGroups;
    private BigDecimal totalAmount;
    private BigDecimal selectedAmount;

    public Long getCartId() { return this.cartId; }
    public void setCartId(Long cartId) { this.cartId = cartId; }
    public List<ShopGroupVO> getShopGroups() { return this.shopGroups; }
    public void setShopGroups(List<ShopGroupVO> shopGroups) { this.shopGroups = shopGroups; }
    public BigDecimal getTotalAmount() { return this.totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public BigDecimal getSelectedAmount() { return this.selectedAmount; }
    public void setSelectedAmount(BigDecimal selectedAmount) { this.selectedAmount = selectedAmount; }
}
