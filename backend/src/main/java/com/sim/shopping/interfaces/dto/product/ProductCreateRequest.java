package com.sim.shopping.interfaces.dto.product;

import java.math.BigDecimal;
import java.util.List;

/**
 * ProductCreate请求对象，封装接口入参
 *
 * @author Sim Team
 * @since 1.0.0
 */
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
     * 获取Skus
     * @return 返回结果
     */
    public List<SkuRequest> getSkus() { return this.skus; }
    /**
     * set Skus
     * @param skus skus
     */
    public void setSkus(List<SkuRequest> skus) { this.skus = skus; }
    /**
     * is Publish
     * @return 返回结果
     */
    public boolean isPublish() { return this.publish; }
    /**
     * set Publish
     * @param publish publish
     */
    public void setPublish(boolean publish) { this.publish = publish; }
}
