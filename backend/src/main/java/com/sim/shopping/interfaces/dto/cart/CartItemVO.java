package com.sim.shopping.interfaces.dto.cart;

import java.math.BigDecimal;

public class CartItemVO {
    private Long cartItemId;
    private Long productId;
    private String productName;
    private String productImage;
    private Long skuId;
    private String skuName;
    private BigDecimal price;
    private Integer quantity;
    private Integer stock;
    private Integer selected;
    private Boolean available;

    public Long getCartItemId() { return this.cartItemId; }
    public void setCartItemId(Long cartItemId) { this.cartItemId = cartItemId; }
    public Long getProductId() { return this.productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductName() { return this.productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getProductImage() { return this.productImage; }
    public void setProductImage(String productImage) { this.productImage = productImage; }
    public Long getSkuId() { return this.skuId; }
    public void setSkuId(Long skuId) { this.skuId = skuId; }
    public String getSkuName() { return this.skuName; }
    public void setSkuName(String skuName) { this.skuName = skuName; }
    public BigDecimal getPrice() { return this.price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getQuantity() { return this.quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Integer getStock() { return this.stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public Integer getSelected() { return this.selected; }
    public void setSelected(Integer selected) { this.selected = selected; }
    public Boolean getAvailable() { return this.available; }
    public void setAvailable(Boolean available) { this.available = available; }
}
