package com.sim.shopping.interfaces.dto.shop;

import java.math.BigDecimal;

public class DashboardResponse {

    private Integer todayOrders;
    private BigDecimal todaySales;
    private Integer totalProducts;
    private Integer publishedProducts;
    private Integer pendingShipments;
    private Integer totalReviews;
    private BigDecimal avgReviewScore;

    public Integer getTodayOrders() { return this.todayOrders; }
    public void setTodayOrders(Integer todayOrders) { this.todayOrders = todayOrders; }
    public BigDecimal getTodaySales() { return this.todaySales; }
    public void setTodaySales(BigDecimal todaySales) { this.todaySales = todaySales; }
    public Integer getTotalProducts() { return this.totalProducts; }
    public void setTotalProducts(Integer totalProducts) { this.totalProducts = totalProducts; }
    public Integer getPublishedProducts() { return this.publishedProducts; }
    public void setPublishedProducts(Integer publishedProducts) { this.publishedProducts = publishedProducts; }
    public Integer getPendingShipments() { return this.pendingShipments; }
    public void setPendingShipments(Integer pendingShipments) { this.pendingShipments = pendingShipments; }
    public Integer getTotalReviews() { return this.totalReviews; }
    public void setTotalReviews(Integer totalReviews) { this.totalReviews = totalReviews; }
    public BigDecimal getAvgReviewScore() { return this.avgReviewScore; }
    public void setAvgReviewScore(BigDecimal avgReviewScore) { this.avgReviewScore = avgReviewScore; }
}
