package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@TableName("t_product_sku")
public class ProductSkuDO extends BaseEntity {
    private String attributes;
    private Long productId;
    private String skuCode;
    private String skuName;
    private BigDecimal price;
    private Integer stock;
    private String imageUrl;

    public String getAttributes() { return this.attributes; }
    public void setAttributes(String attributes) { this.attributes = attributes; }
    public Long getProductId() { return this.productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getSkuCode() { return this.skuCode; }
    public void setSkuCode(String skuCode) { this.skuCode = skuCode; }
    public String getSkuName() { return this.skuName; }
    public void setSkuName(String skuName) { this.skuName = skuName; }
    public BigDecimal getPrice() { return this.price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getStock() { return this.stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public String getImageUrl() { return this.imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
