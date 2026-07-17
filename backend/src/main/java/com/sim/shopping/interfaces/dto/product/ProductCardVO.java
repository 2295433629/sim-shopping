package com.sim.shopping.interfaces.dto.product;

import java.math.BigDecimal;

/**
 * ProductCard视图对象，用于前端展示
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class ProductCardVO {
    private Long productId;
    private String name;
    private String subtitle;
    private String mainImage;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer sales;
    private Long shopId;
    private String shopName;

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
    /** 获取Name */
    public String getName() { return this.name; }
    /** set Name */
    public void setName(String name) { this.name = name; }
    /**
     * 获取Subtitle
     * @return 返回结果
     */
    public String getSubtitle() { return this.subtitle; }
    /**
     * set Subtitle
     * @param subtitle subtitle
     */
    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }
    /**
     * 获取Main Image
     * @return 返回结果
     */
    public String getMainImage() { return this.mainImage; }
    /**
     * set Main Image
     * @param mainImage mainImage
     */
    public void setMainImage(String mainImage) { this.mainImage = mainImage; }
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
     * 获取Original Price
     * @return 返回结果
     */
    public BigDecimal getOriginalPrice() { return this.originalPrice; }
    /**
     * set Original Price
     * @param originalPrice originalPrice
     */
    public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }
    /**
     * 获取Sales
     * @return 返回结果
     */
    public Integer getSales() { return this.sales; }
    /**
     * set Sales
     * @param sales sales
     */
    public void setSales(Integer sales) { this.sales = sales; }
    /**
     * 获取Shop Id
     * @return 返回结果
     */
    public Long getShopId() { return this.shopId; }
    /**
     * set Shop Id
     * @param shopId shopId
     */
    public void setShopId(Long shopId) { this.shopId = shopId; }
    /**
     * 获取Shop Name
     * @return 返回结果
     */
    public String getShopName() { return this.shopName; }
    /**
     * set Shop Name
     * @param shopName shopName
     */
    public void setShopName(String shopName) { this.shopName = shopName; }
}
