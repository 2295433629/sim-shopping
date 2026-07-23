package com.sim.shopping.interfaces.dto.product;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品详情视图对象，包含商品完整信息和SKU列表
 *
 * @author Sim Team
 * @since 1.0.0
 */
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
    private String status;
    private List<SkuVO> skus;
    private ReviewSummaryVO reviewSummary;

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
     * 获取Images
     * @return 返回结果
     */
    public List<String> getImages() { return this.images; }
    /**
     * set Images
     * @param images images
     */
    public void setImages(List<String> images) { this.images = images; }
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
     * 获取Category Name
     * @return 返回结果
     */
    public String getCategoryName() { return this.categoryName; }
    /**
     * set Category Name
     * @param categoryName categoryName
     */
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
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
     * 获取Skus
     * @return 返回结果
     */
    public List<SkuVO> getSkus() { return this.skus; }
    /**
     * set Skus
     * @param skus skus
     */
    public void setSkus(List<SkuVO> skus) { this.skus = skus; }
    /**
     * 获取Review Summary
     * @return 返回结果
     */
    public ReviewSummaryVO getReviewSummary() { return this.reviewSummary; }
    /**
     * set Review Summary
     * @param reviewSummary reviewSummary
     */
    public void setReviewSummary(ReviewSummaryVO reviewSummary) { this.reviewSummary = reviewSummary; }
}
