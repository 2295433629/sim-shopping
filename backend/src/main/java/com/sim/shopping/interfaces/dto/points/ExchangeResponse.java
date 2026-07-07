package com.sim.shopping.interfaces.dto.points;

import java.time.LocalDateTime;

public class ExchangeResponse {

    private Long recordId;
    private Long productId;
    private String productName;
    private Integer quantity;
    private Integer totalPoints;
    private Integer remainingPoints;
    private LocalDateTime exchangeTime;

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Integer getRemainingPoints() {
        return remainingPoints;
    }

    public void setRemainingPoints(Integer remainingPoints) {
        this.remainingPoints = remainingPoints;
    }

    public LocalDateTime getExchangeTime() {
        return exchangeTime;
    }

    public void setExchangeTime(LocalDateTime exchangeTime) {
        this.exchangeTime = exchangeTime;
    }
}
