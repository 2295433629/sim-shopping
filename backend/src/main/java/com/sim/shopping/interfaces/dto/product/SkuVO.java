package com.sim.shopping.interfaces.dto.product;

import java.math.BigDecimal;

/**
 * Sku视图对象，用于前端展示
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class SkuVO {
    private Long skuId;
    private String skuName;
    private BigDecimal price;
    private Integer stock;

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
}
