package com.sim.shopping.interfaces.dto.product;

import java.math.BigDecimal;

public class SkuVO {
    private Long skuId;
    private String skuName;
    private BigDecimal price;
    private Integer stock;

    public Long getSkuId() { return this.skuId; }
    public void setSkuId(Long skuId) { this.skuId = skuId; }
    public String getSkuName() { return this.skuName; }
    public void setSkuName(String skuName) { this.skuName = skuName; }
    public BigDecimal getPrice() { return this.price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getStock() { return this.stock; }
    public void setStock(Integer stock) { this.stock = stock; }
}
