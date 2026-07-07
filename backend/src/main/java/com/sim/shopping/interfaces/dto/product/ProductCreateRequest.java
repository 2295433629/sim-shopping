package com.sim.shopping.interfaces.dto.product;

import java.math.BigDecimal;
import java.util.List;

public class ProductCreateRequest {
    private String name;
    private String subtitle;
    private String description;
    private Long categoryId;
    private Long brandId;
    private String mainImage;
    private List<String> images;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer stock;
    private List<SkuRequest> skus;
    private boolean publish;

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }
    public String getSubtitle() { return this.subtitle; }
    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }
    public String getDescription() { return this.description; }
    public void setDescription(String description) { this.description = description; }
    public Long getCategoryId() { return this.categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public Long getBrandId() { return this.brandId; }
    public void setBrandId(Long brandId) { this.brandId = brandId; }
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
    public List<SkuRequest> getSkus() { return this.skus; }
    public void setSkus(List<SkuRequest> skus) { this.skus = skus; }
    public boolean isPublish() { return this.publish; }
    public void setPublish(boolean publish) { this.publish = publish; }
}
