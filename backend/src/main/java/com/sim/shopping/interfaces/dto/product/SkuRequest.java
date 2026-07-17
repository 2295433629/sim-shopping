package com.sim.shopping.interfaces.dto.product;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Sku请求对象，封装接口入参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class SkuRequest {
    private String skuName;
    private BigDecimal price;
    private Integer stock;
    private Map<String, String> attributes;

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
     * 获取Attributes
     * @return 返回结果
     */
    public Map<String, String> getAttributes() { return this.attributes; }
    /**
     * set Attributes
     * @param attributes attributes
     */
    public void setAttributes(Map<String, String> attributes) { this.attributes = attributes; }
}
