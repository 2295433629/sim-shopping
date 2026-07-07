package com.sim.shopping.interfaces.dto.product;

import java.math.BigDecimal;

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

    public Long getProductId() { return this.productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }
    public String getSubtitle() { return this.subtitle; }
    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }
    public String getMainImage() { return this.mainImage; }
    public void setMainImage(String mainImage) { this.mainImage = mainImage; }
    public BigDecimal getPrice() { return this.price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public BigDecimal getOriginalPrice() { return this.originalPrice; }
    public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }
    public Integer getSales() { return this.sales; }
    public void setSales(Integer sales) { this.sales = sales; }
    public Long getShopId() { return this.shopId; }
    public void setShopId(Long shopId) { this.shopId = shopId; }
    public String getShopName() { return this.shopName; }
    public void setShopName(String shopName) { this.shopName = shopName; }
}
