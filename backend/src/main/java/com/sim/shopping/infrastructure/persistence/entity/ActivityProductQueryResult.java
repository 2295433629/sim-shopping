package com.sim.shopping.infrastructure.persistence.entity;

import java.math.BigDecimal;

/**
 * 活动商品查询结果，用于 Mapper 自定义 SQL 返回，包含商品关联信息和商品基本信息
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class ActivityProductQueryResult {

    private Long id;
    private Long productId;
    private String productName;
    private String productImage;
    private BigDecimal price;
    private Integer sortOrder;

    /** 获取Id */
    public Long getId() { return this.id; }
    /** set Id */
    public void setId(Long id) { this.id = id; }
    /** 获取Product Id */
    public Long getProductId() { return this.productId; }
    /** set Product Id */
    public void setProductId(Long productId) { this.productId = productId; }
    /** 获取Product Name */
    public String getProductName() { return this.productName; }
    /** set Product Name */
    public void setProductName(String productName) { this.productName = productName; }
    /** 获取Product Image */
    public String getProductImage() { return this.productImage; }
    /** set Product Image */
    public void setProductImage(String productImage) { this.productImage = productImage; }
    /** 获取Price */
    public BigDecimal getPrice() { return this.price; }
    /** set Price */
    public void setPrice(BigDecimal price) { this.price = price; }
    /** 获取Sort Order */
    public Integer getSortOrder() { return this.sortOrder; }
    /** set Sort Order */
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
}
