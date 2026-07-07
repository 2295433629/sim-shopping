package com.sim.shopping.interfaces.dto.product;

import java.math.BigDecimal;
import java.util.List;

public class ProductDetailVO {
    private Long productId;
    private String name;
    private String subtitle;
    private String description;
    private String mainImage;
    private List<String> images;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer stock;
    private Integer sales;
    private Integer viewCount;
    private Long categoryId;
    private String categoryName;
    private Long shopId;
    private String shopName;
    private List<SkuVO> skus;
    private ReviewSummaryVO reviewSummary;

    public Long getProductId() { return this.productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }
    public String getSubtitle() { return this.subtitle; }
    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }
    public String getDescription() { return this.description; }
    public void setDescription(String description) { this.description = description; }
    public String getMainImage() { return this.mainImage; }
    public void setMainImage(String mainImage) { this.mainImage = mainImage; }
    public List<String> getImages() { return this.images; }
    public void setImages(List<String> images) { this.images = images; }
    public BigDecimal getPrice() { return this.price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public BigDecimal getOriginalPrice() { return this.originalPrice; }
    public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }
    public Integer getStock() { return this.stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public Integer getSales() { return this.sales; }
    public void setSales(Integer sales) { this.sales = sales; }
    public Integer getViewCount() { return this.viewCount; }
    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }
    public Long getCategoryId() { return this.categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public String getCategoryName() { return this.categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public Long getShopId() { return this.shopId; }
    public void setShopId(Long shopId) { this.shopId = shopId; }
    public String getShopName() { return this.shopName; }
    public void setShopName(String shopName) { this.shopName = shopName; }
    public List<SkuVO> getSkus() { return this.skus; }
    public void setSkus(List<SkuVO> skus) { this.skus = skus; }
    public ReviewSummaryVO getReviewSummary() { return this.reviewSummary; }
    public void setReviewSummary(ReviewSummaryVO reviewSummary) { this.reviewSummary = reviewSummary; }
}
