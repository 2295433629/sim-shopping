package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@TableName("t_cart_item")
public class CartItemDO extends BaseEntity {
    private Long cartId;
    private Long productId;
    private Integer selected;
    private Integer quantity;
    private Long shopId;
    private Long skuId;

    public Long getCartId() { return this.cartId; }
    public void setCartId(Long cartId) { this.cartId = cartId; }
    public Long getProductId() { return this.productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Integer getSelected() { return this.selected; }
    public void setSelected(Integer selected) { this.selected = selected; }
    public Integer getQuantity() { return this.quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Long getShopId() { return this.shopId; }
    public void setShopId(Long shopId) { this.shopId = shopId; }
    public Long getSkuId() { return this.skuId; }
    public void setSkuId(Long skuId) { this.skuId = skuId; }
}
