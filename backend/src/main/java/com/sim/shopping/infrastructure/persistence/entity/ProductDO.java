package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * 商品实体，对应 t_product 表，存储商品基本信息
 *
 * @author Sim Team
 * @since 1.0.0
 */
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
     * 获取Avg Review Score
     * @return 返回结果
     */
    public BigDecimal getAvgReviewScore() { return this.avgReviewScore; }
    /**
     * set Avg Review Score
     * @param avgReviewScore avgReviewScore
     */
    public void setAvgReviewScore(BigDecimal avgReviewScore) { this.avgReviewScore = avgReviewScore; }
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
     * 获取Favorite Count
     * @return 返回结果
     */
    public Integer getFavoriteCount() { return this.favoriteCount; }
    /**
     * set Favorite Count
     * @param favoriteCount favoriteCount
     */
    public void setFavoriteCount(Integer favoriteCount) { this.favoriteCount = favoriteCount; }
    /**
     * 获取Description
     * @return 返回结果
     */
    public String getDescription() { return this.description; }
    /**
     * set Description
     * @param description description
     */
    public void setDescription(String description) { this.description = description; }
    /**
     * 获取View Count
     * @return 返回结果
     */
    public Integer getViewCount() { return this.viewCount; }
    /**
     * set View Count
     * @param viewCount viewCount
     */
    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }
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
     * 获取Is New
     * @return 返回结果
     */
    public Integer getIsNew() { return this.isNew; }
    /**
     * set Is New
     * @param isNew isNew
     */
    public void setIsNew(Integer isNew) { this.isNew = isNew; }
    /**
     * 获取Is Recommended
     * @return 返回结果
     */
    public Integer getIsRecommended() { return this.isRecommended; }
    /**
     * set Is Recommended
     * @param isRecommended isRecommended
     */
    public void setIsRecommended(Integer isRecommended) { this.isRecommended = isRecommended; }
    /**
     * 获取Category Id
     * @return 返回结果
     */
    public Long getCategoryId() { return this.categoryId; }
    /**
     * set Category Id
     * @param categoryId categoryId
     */
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    /**
     * 获取Brand Id
     * @return 返回结果
     */
    public Long getBrandId() { return this.brandId; }
    /**
     * set Brand Id
     * @param brandId brandId
     */
    public void setBrandId(Long brandId) { this.brandId = brandId; }
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
     * 获取Review Count
     * @return 返回结果
     */
    public Integer getReviewCount() { return this.reviewCount; }
    /**
     * set Review Count
     * @param reviewCount reviewCount
     */
    public void setReviewCount(Integer reviewCount) { this.reviewCount = reviewCount; }
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
    /** 获取Name */
    public String getName() { return this.name; }
    /** set Name */
    public void setName(String name) { this.name = name; }
    /**
     * 获取Status
     * @return 返回结果
     */
    public String getStatus() { return this.status; }
    /**
     * set Status
     * @param status status
     */
    public void setStatus(String status) { this.status = status; }
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
     * 获取Publish Time
     * @return 返回结果
     */
    public LocalDateTime getPublishTime() { return this.publishTime; }
    /**
     * set Publish Time
     * @param publishTime publishTime
     */
    public void setPublishTime(LocalDateTime publishTime) { this.publishTime = publishTime; }
}
