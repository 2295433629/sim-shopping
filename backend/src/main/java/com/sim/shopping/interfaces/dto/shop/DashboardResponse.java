package com.sim.shopping.interfaces.dto.shop;

import java.math.BigDecimal;

/**
 * Dashboard响应对象，封装接口出参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class DashboardResponse {

    private Integer todayOrders;
    private BigDecimal todaySales;
    private Integer totalProducts;
    private Integer publishedProducts;
    private Integer pendingShipments;
    private Integer totalReviews;
    private BigDecimal avgReviewScore;

    /**
     * 获取Today Orders
     * @return 返回结果
     */
    public Integer getTodayOrders() { return this.todayOrders; }
    /**
     * set Today Orders
     * @param todayOrders todayOrders
     */
    public void setTodayOrders(Integer todayOrders) { this.todayOrders = todayOrders; }
    /**
     * 获取Today Sales
     * @return 返回结果
     */
    public BigDecimal getTodaySales() { return this.todaySales; }
    /**
     * set Today Sales
     * @param todaySales todaySales
     */
    public void setTodaySales(BigDecimal todaySales) { this.todaySales = todaySales; }
    /**
     * 获取Total Products
     * @return 返回结果
     */
    public Integer getTotalProducts() { return this.totalProducts; }
    /**
     * set Total Products
     * @param totalProducts totalProducts
     */
    public void setTotalProducts(Integer totalProducts) { this.totalProducts = totalProducts; }
    /**
     * 获取Published Products
     * @return 返回结果
     */
    public Integer getPublishedProducts() { return this.publishedProducts; }
    /**
     * set Published Products
     * @param publishedProducts publishedProducts
     */
    public void setPublishedProducts(Integer publishedProducts) { this.publishedProducts = publishedProducts; }
    /**
     * 获取Pending Shipments
     * @return 返回结果
     */
    public Integer getPendingShipments() { return this.pendingShipments; }
    /**
     * set Pending Shipments
     * @param pendingShipments pendingShipments
     */
    public void setPendingShipments(Integer pendingShipments) { this.pendingShipments = pendingShipments; }
    /**
     * 获取Total Reviews
     * @return 返回结果
     */
    public Integer getTotalReviews() { return this.totalReviews; }
    /**
     * set Total Reviews
     * @param totalReviews totalReviews
     */
    public void setTotalReviews(Integer totalReviews) { this.totalReviews = totalReviews; }
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
}
