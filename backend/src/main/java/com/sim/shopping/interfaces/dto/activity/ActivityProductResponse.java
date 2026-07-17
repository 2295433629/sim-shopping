package com.sim.shopping.interfaces.dto.activity;

import java.math.BigDecimal;

/**
 * ActivityProduct响应对象，封装接口出参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class ActivityProductResponse {

    private Long productId;
    private String productName;
    private String productImage;
    private BigDecimal price;
    private Integer sortOrder;

    /**
     * 获取Product Id
     * @return 返回结果
     */
    public Long getProductId() {
        return this.productId;
    }

    /**
     * set Product Id
     * @param productId productId
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     * 获取Product Name
     * @return 返回结果
     */
    public String getProductName() {
        return this.productName;
    }

    /**
     * set Product Name
     * @param productName productName
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * 获取Product Image
     * @return 返回结果
     */
    public String getProductImage() {
        return this.productImage;
    }

    /**
     * set Product Image
     * @param productImage productImage
     */
    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    /**
     * 获取Price
     * @return 返回结果
     */
    public BigDecimal getPrice() {
        return this.price;
    }

    /**
     * set Price
     * @param price price
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 获取Sort Order
     * @return 返回结果
     */
    public Integer getSortOrder() {
        return this.sortOrder;
    }

    /**
     * set Sort Order
     * @param sortOrder sortOrder
     */
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}
