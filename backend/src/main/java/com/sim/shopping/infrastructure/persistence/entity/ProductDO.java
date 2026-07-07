package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@TableName("t_product")
public class ProductDO extends BaseEntity {
    private String mainImage;
    private BigDecimal avgReviewScore;
    private BigDecimal price;
    private Integer favoriteCount;
    private String description;
    private Integer viewCount;
    private BigDecimal originalPrice;
    private Integer stock;
    private Integer isNew;
    private Integer isRecommended;
    private Long categoryId;
    private Long brandId;
    private Long shopId;
    private Integer reviewCount;
    private String subtitle;
    private String name;
    private String status;
    private Integer sales;
    private LocalDateTime publishTime;

    public String getMainImage() { return this.mainImage; }
    public void setMainImage(String mainImage) { this.mainImage = mainImage; }
    public BigDecimal getAvgReviewScore() { return this.avgReviewScore; }
    public void setAvgReviewScore(BigDecimal avgReviewScore) { this.avgReviewScore = avgReviewScore; }
    public BigDecimal getPrice() { return this.price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getFavoriteCount() { return this.favoriteCount; }
    public void setFavoriteCount(Integer favoriteCount) { this.favoriteCount = favoriteCount; }
    public String getDescription() { return this.description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getViewCount() { return this.viewCount; }
    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }
    public BigDecimal getOriginalPrice() { return this.originalPrice; }
    public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }
    public Integer getStock() { return this.stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public Integer getIsNew() { return this.isNew; }
    public void setIsNew(Integer isNew) { this.isNew = isNew; }
    public Integer getIsRecommended() { return this.isRecommended; }
    public void setIsRecommended(Integer isRecommended) { this.isRecommended = isRecommended; }
    public Long getCategoryId() { return this.categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public Long getBrandId() { return this.brandId; }
    public void setBrandId(Long brandId) { this.brandId = brandId; }
    public Long getShopId() { return this.shopId; }
    public void setShopId(Long shopId) { this.shopId = shopId; }
    public Integer getReviewCount() { return this.reviewCount; }
    public void setReviewCount(Integer reviewCount) { this.reviewCount = reviewCount; }
    public String getSubtitle() { return this.subtitle; }
    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }
    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }
    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getSales() { return this.sales; }
    public void setSales(Integer sales) { this.sales = sales; }
    public LocalDateTime getPublishTime() { return this.publishTime; }
    public void setPublishTime(LocalDateTime publishTime) { this.publishTime = publishTime; }
}
