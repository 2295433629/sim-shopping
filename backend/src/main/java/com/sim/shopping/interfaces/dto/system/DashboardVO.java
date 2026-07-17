package com.sim.shopping.interfaces.dto.system;

import com.sim.shopping.interfaces.dto.order.OrderListItemVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * Dashboard视图对象，用于前端展示
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class DashboardVO {
    private long totalUsers;
    private long totalMerchants;
    private long totalOrders;
    private long totalProducts;
    private BigDecimal totalSales;
    private long todayOrders;
    private BigDecimal todaySales;
    private long pendingMerchants;
    private List<OrderListItemVO> recentOrders;

    /**
     * 获取Total Users
     * @return 返回结果
     */
    public long getTotalUsers() { return this.totalUsers; }
    /**
     * set Total Users
     * @param totalUsers totalUsers
     */
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }
    /**
     * 获取Total Merchants
     * @return 返回结果
     */
    public long getTotalMerchants() { return this.totalMerchants; }
    /**
     * set Total Merchants
     * @param totalMerchants totalMerchants
     */
    public void setTotalMerchants(long totalMerchants) { this.totalMerchants = totalMerchants; }
    /**
     * 获取Total Orders
     * @return 返回结果
     */
    public long getTotalOrders() { return this.totalOrders; }
    /**
     * set Total Orders
     * @param totalOrders totalOrders
     */
    public void setTotalOrders(long totalOrders) { this.totalOrders = totalOrders; }
    /**
     * 获取Total Products
     * @return 返回结果
     */
    public long getTotalProducts() { return this.totalProducts; }
    /**
     * set Total Products
     * @param totalProducts totalProducts
     */
    public void setTotalProducts(long totalProducts) { this.totalProducts = totalProducts; }
    /**
     * 获取Total Sales
     * @return 返回结果
     */
    public BigDecimal getTotalSales() { return this.totalSales; }
    /**
     * set Total Sales
     * @param totalSales totalSales
     */
    public void setTotalSales(BigDecimal totalSales) { this.totalSales = totalSales; }
    /**
     * 获取Today Orders
     * @return 返回结果
     */
    public long getTodayOrders() { return this.todayOrders; }
    /**
     * set Today Orders
     * @param todayOrders todayOrders
     */
    public void setTodayOrders(long todayOrders) { this.todayOrders = todayOrders; }
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
     * 获取Pending Merchants
     * @return 返回结果
     */
    public long getPendingMerchants() { return this.pendingMerchants; }
    /**
     * set Pending Merchants
     * @param pendingMerchants pendingMerchants
     */
    public void setPendingMerchants(long pendingMerchants) { this.pendingMerchants = pendingMerchants; }
    /**
     * 获取Recent Orders
     * @return 返回结果
     */
    public List<OrderListItemVO> getRecentOrders() { return this.recentOrders; }
    /**
     * set Recent Orders
     * @param recentOrders recentOrders
     */
    public void setRecentOrders(List<OrderListItemVO> recentOrders) { this.recentOrders = recentOrders; }
}
