package com.sim.shopping.interfaces.dto.flashsale;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * FlashSaleDetail响应对象，封装接口出参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class FlashSaleDetailResponse {

    private Long saleId;
    private Long productId;
    private String productName;
    private String productImage;
    private BigDecimal originalPrice;
    private BigDecimal flashPrice;
    private Integer stock;
    private Integer soldCount;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer limitPerUser;
    private String status;

    /**
     * 获取Sale Id
     * @return 返回结果
     */
    public Long getSaleId() {
        return this.saleId;
    }

    /**
     * set Sale Id
     * @param saleId saleId
     */
    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }

    /**
     * 获取Product Id
     * @return 返回结果
     */
    public Long getProductId() {
        return this.productId;
    }

    /**
     * set Product Id
     * @param productId productId
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     * 获取Product Name
     * @return 返回结果
     */
    public String getProductName() {
        return this.productName;
    }

    /**
     * set Product Name
     * @param productName productName
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * 获取Product Image
     * @return 返回结果
     */
    public String getProductImage() {
        return this.productImage;
    }

    /**
     * set Product Image
     * @param productImage productImage
     */
    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    /**
     * 获取Original Price
     * @return 返回结果
     */
    public BigDecimal getOriginalPrice() {
        return this.originalPrice;
    }

    /**
     * set Original Price
     * @param originalPrice originalPrice
     */
    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    /**
     * 获取Flash Price
     * @return 返回结果
     */
    public BigDecimal getFlashPrice() {
        return this.flashPrice;
    }

    /**
     * set Flash Price
     * @param flashPrice flashPrice
     */
    public void setFlashPrice(BigDecimal flashPrice) {
        this.flashPrice = flashPrice;
    }

    /**
     * 获取Stock
     * @return 返回结果
     */
    public Integer getStock() {
        return this.stock;
    }

    /**
     * set Stock
     * @param stock stock
     */
    public void setStock(Integer stock) {
        this.stock = stock;
    }

    /**
     * 获取Sold Count
     * @return 返回结果
     */
    public Integer getSoldCount() {
        return this.soldCount;
    }

    /**
     * set Sold Count
     * @param soldCount soldCount
     */
    public void setSoldCount(Integer soldCount) {
        this.soldCount = soldCount;
    }

    /**
     * 获取Start Time
     * @return 返回结果
     */
    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    /**
     * set Start Time
     * @param startTime startTime
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取End Time
     * @return 返回结果
     */
    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    /**
     * set End Time
     * @param endTime endTime
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取Limit Per User
     * @return 返回结果
     */
    public Integer getLimitPerUser() {
        return this.limitPerUser;
    }

    /**
     * set Limit Per User
     * @param limitPerUser limitPerUser
     */
    public void setLimitPerUser(Integer limitPerUser) {
        this.limitPerUser = limitPerUser;
    }

    /**
     * 获取Status
     * @return 返回结果
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * set Status
     * @param status status
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
