package com.sim.shopping.interfaces.dto.product;

import java.math.BigDecimal;
import java.util.Map;

public class SkuRequest {
    private String skuName;
    private BigDecimal price;
    private Integer stock;
    private Map<String, String> attributes;

    public String getSkuName() { return this.skuName; }
    public void setSkuName(String skuName) { this.skuName = skuName; }
    public BigDecimal getPrice() { return this.price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getStock() { return this.stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public Map<String, String> getAttributes() { return this.attributes; }
    public void setAttributes(Map<String, String> attributes) { this.attributes = attributes; }
}
