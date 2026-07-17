package com.sim.shopping.interfaces.dto.points;

import java.time.LocalDateTime;

/**
 * Exchange响应对象，封装接口出参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class ExchangeResponse {

    private Long recordId;
    private Long productId;
    private String productName;
    private Integer quantity;
    private Integer totalPoints;
    private Integer remainingPoints;
    private LocalDateTime exchangeTime;

    /**
     * 获取Record Id
     * @return 返回结果
     */
    public Long getRecordId() {
        return recordId;
    }

    /**
     * set Record Id
     * @param recordId recordId
     */
    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    /**
     * 获取Product Id
     * @return 返回结果
     */
    public Long getProductId() {
        return productId;
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
     * 获取Quantity
     * @return 返回结果
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * set Quantity
     * @param quantity quantity
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * 获取Total Points
     * @return 返回结果
     */
    public Integer getTotalPoints() {
        return totalPoints;
    }

    /**
     * set Total Points
     * @param totalPoints totalPoints
     */
    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    /**
     * 获取Remaining Points
     * @return 返回结果
     */
    public Integer getRemainingPoints() {
        return remainingPoints;
    }

    /**
     * set Remaining Points
     * @param remainingPoints remainingPoints
     */
    public void setRemainingPoints(Integer remainingPoints) {
        this.remainingPoints = remainingPoints;
    }

    /**
     * 获取Exchange Time
     * @return 返回结果
     */
    public LocalDateTime getExchangeTime() {
        return exchangeTime;
    }

    /**
     * set Exchange Time
     * @param exchangeTime exchangeTime
     */
    public void setExchangeTime(LocalDateTime exchangeTime) {
        this.exchangeTime = exchangeTime;
    }
}
