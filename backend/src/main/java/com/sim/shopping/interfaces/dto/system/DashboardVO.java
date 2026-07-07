package com.sim.shopping.interfaces.dto.system;

import com.sim.shopping.interfaces.dto.order.OrderListItemVO;

import java.math.BigDecimal;
import java.util.List;

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

    public long getTotalUsers() { return this.totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }
    public long getTotalMerchants() { return this.totalMerchants; }
    public void setTotalMerchants(long totalMerchants) { this.totalMerchants = totalMerchants; }
    public long getTotalOrders() { return this.totalOrders; }
    public void setTotalOrders(long totalOrders) { this.totalOrders = totalOrders; }
    public long getTotalProducts() { return this.totalProducts; }
    public void setTotalProducts(long totalProducts) { this.totalProducts = totalProducts; }
    public BigDecimal getTotalSales() { return this.totalSales; }
    public void setTotalSales(BigDecimal totalSales) { this.totalSales = totalSales; }
    public long getTodayOrders() { return this.todayOrders; }
    public void setTodayOrders(long todayOrders) { this.todayOrders = todayOrders; }
    public BigDecimal getTodaySales() { return this.todaySales; }
    public void setTodaySales(BigDecimal todaySales) { this.todaySales = todaySales; }
    public long getPendingMerchants() { return this.pendingMerchants; }
    public void setPendingMerchants(long pendingMerchants) { this.pendingMerchants = pendingMerchants; }
    public List<OrderListItemVO> getRecentOrders() { return this.recentOrders; }
    public void setRecentOrders(List<OrderListItemVO> recentOrders) { this.recentOrders = recentOrders; }
}
