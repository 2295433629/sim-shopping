package com.sim.shopping.interfaces.dto.cart;

import java.math.BigDecimal;
import java.util.List;

/**
 * Cart响应对象，封装接口出参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class CartResponse {
    private Long cartId;
    private List<ShopGroupVO> shopGroups;
    private BigDecimal totalAmount;
    private BigDecimal selectedAmount;

    /**
     * 获取Cart Id
     * @return 返回结果
     */
    public Long getCartId() { return this.cartId; }
    /**
     * set Cart Id
     * @param cartId cartId
     */
    public void setCartId(Long cartId) { this.cartId = cartId; }
    /**
     * 获取Shop Groups
     * @return 返回结果
     */
    public List<ShopGroupVO> getShopGroups() { return this.shopGroups; }
    /**
     * set Shop Groups
     * @param shopGroups shopGroups
     */
    public void setShopGroups(List<ShopGroupVO> shopGroups) { this.shopGroups = shopGroups; }
    /**
     * 获取Total Amount
     * @return 返回结果
     */
    public BigDecimal getTotalAmount() { return this.totalAmount; }
    /**
     * set Total Amount
     * @param totalAmount totalAmount
     */
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    /**
     * 获取Selected Amount
     * @return 返回结果
     */
    public BigDecimal getSelectedAmount() { return this.selectedAmount; }
    /**
     * set Selected Amount
     * @param selectedAmount selectedAmount
     */
    public void setSelectedAmount(BigDecimal selectedAmount) { this.selectedAmount = selectedAmount; }
}
