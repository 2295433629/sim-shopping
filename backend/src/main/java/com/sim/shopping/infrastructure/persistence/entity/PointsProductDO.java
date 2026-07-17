package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 积分商品实体，对应 t_points_product 表，存储积分兑换商品
 *
 * @author Sim Team
 * @since 1.0.0
 */
@TableName("t_points_product")
public class PointsProductDO extends BaseEntity {

    @TableField("product_name")
    private String productName;

    @TableField("description")
    private String description;

    @TableField("image_url")
    private String imageUrl;

    @TableField("points_required")
    private Integer pointsRequired;

    @TableField("stock")
    private Integer stock;

    @TableField("status")
    private String status;

    /**
     * 获取Product Name
     * @return 返回结果
     */
    public String getProductName() {
        return productName;
    }

    /**
     * set Product Name
     * @param productName productName
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * 获取Description
     * @return 返回结果
     */
    public String getDescription() {
        return description;
    }

    /**
     * set Description
     * @param description description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取Image Url
     * @return 返回结果
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * set Image Url
     * @param imageUrl imageUrl
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * 获取Points Required
     * @return 返回结果
     */
    public Integer getPointsRequired() {
        return pointsRequired;
    }

    /**
     * set Points Required
     * @param pointsRequired pointsRequired
     */
    public void setPointsRequired(Integer pointsRequired) {
        this.pointsRequired = pointsRequired;
    }

    /**
     * 获取Stock
     * @return 返回结果
     */
    public Integer getStock() {
        return stock;
    }

    /**
     * set Stock
     * @param stock stock
     */
    public void setStock(Integer stock) {
        this.stock = stock;
    }

    /**
     * 获取Status
     * @return 返回结果
     */
    public String getStatus() {
        return status;
    }

    /**
     * set Status
     * @param status status
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
