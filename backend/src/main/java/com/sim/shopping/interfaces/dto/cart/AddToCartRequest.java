package com.sim.shopping.interfaces.dto.cart;

public class AddToCartRequest {
    private Long productId;
    private Long skuId;
    private Integer quantity;

    public Long getProductId() { return this.productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Long getSkuId() { return this.skuId; }
    public void setSkuId(Long skuId) { this.skuId = skuId; }
    public Integer getQuantity() { return this.quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
