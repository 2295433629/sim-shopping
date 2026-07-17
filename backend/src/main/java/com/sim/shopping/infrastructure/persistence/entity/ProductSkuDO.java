package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * 商品SKU实体，对应 t_product_sku 表，存储商品规格信息
 *
 * @author Sim Team
 * @since 1.0.0
 */
@TableName("t_product_sku")
public class ProductSkuDO extends BaseEntity {
    private String attributes;
    private Long productId;
    private String skuCode;
    private String skuName;
    private BigDecimal price;
    private Integer stock;
    private String imageUrl;

    /**
     * 获取Attributes
     * @return 返回结果
     */
    public String getAttributes() { return this.attributes; }
    /**
     * set Attributes
     * @param attributes attributes
     */
    public void setAttributes(String attributes) { this.attributes = attributes; }
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
     * 获取Sku Code
     * @return 返回结果
     */
    public String getSkuCode() { return this.skuCode; }
    /**
     * set Sku Code
     * @param skuCode skuCode
     */
    public void setSkuCode(String skuCode) { this.skuCode = skuCode; }
    /**
     * 获取Sku Name
     * @return 返回结果
     */
    public String getSkuName() { return this.skuName; }
    /**
     * set Sku Name
     * @param skuName skuName
     */
    public void setSkuName(String skuName) { this.skuName = skuName; }
    /**
     * 获取Price
     * @return 返回结果
     */
    public BigDecimal getPrice() { return this.price; }
    /**
     * set Price
     * @param price price
     */
    public void setPrice(BigDecimal price) { this.price = price; }
    /**
     * 获取Stock
     * @return 返回结果
     */
    public Integer getStock() { return this.stock; }
    /**
     * set Stock
     * @param stock stock
     */
    public void setStock(Integer stock) { this.stock = stock; }
    /**
     * 获取Image Url
     * @return 返回结果
     */
    public String getImageUrl() { return this.imageUrl; }
    /**
     * set Image Url
     * @param imageUrl imageUrl
     */
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
