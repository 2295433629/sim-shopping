package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * ProductImage数据实体，对应数据库表
 *
 * @author Sim Team
 * @since 1.0.0
 */
@TableName("t_product_image")
public class ProductImageDO extends BaseEntity {
    private Integer sortOrder;
    private Integer imageType;
    private Long productId;
    private String imageUrl;

    /**
     * 获取Sort Order
     * @return 返回结果
     */
    public Integer getSortOrder() { return this.sortOrder; }
    /**
     * set Sort Order
     * @param sortOrder sortOrder
     */
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    /**
     * 获取Image Type
     * @return 返回结果
     */
    public Integer getImageType() { return this.imageType; }
    /**
     * set Image Type
     * @param imageType imageType
     */
    public void setImageType(Integer imageType) { this.imageType = imageType; }
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
    /**
     * 获取Image Url
     * @return 返回结果
     */
    public String getImageUrl() { return this.imageUrl; }
    /**
     * set Image Url
     * @param imageUrl imageUrl
     */
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
