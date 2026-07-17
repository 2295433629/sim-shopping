package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * 购物车项实体，对应 t_cart_item 表，存储购物车中的商品
 *
 * @author Sim Team
 * @since 1.0.0
 */
@TableName("t_cart_item")
public class CartItemDO extends BaseEntity {
    private Long cartId;
    private Long productId;
    private Integer selected;
    private Integer quantity;
    private Long shopId;
    private Long skuId;

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
     * 获取Product Id
     * @return 返回结果
     */
    public Long getProductId() { return this.productId; }
    /**
     * set Product Id
     * @param productId productId
     */
    public void setProductId(Long productId) { this.productId = productId; }
    /**
     * 获取Selected
     * @return 返回结果
     */
    public Integer getSelected() { return this.selected; }
    /**
     * set Selected
     * @param selected selected
     */
    public void setSelected(Integer selected) { this.selected = selected; }
    /**
     * 获取Quantity
     * @return 返回结果
     */
    public Integer getQuantity() { return this.quantity; }
    /**
     * set Quantity
     * @param quantity quantity
     */
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
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
     * 获取Sku Id
     * @return 返回结果
     */
    public Long getSkuId() { return this.skuId; }
    /**
     * set Sku Id
     * @param skuId skuId
     */
    public void setSkuId(Long skuId) { this.skuId = skuId; }
}
