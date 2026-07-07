package com.sim.shopping.interfaces.dto.points;

public class PointsStatisticsVO {

    private Long totalUsers;
    private Integer totalEarned;
    private Integer totalSpent;
    private Integer totalExchanges;

    public PointsStatisticsVO() {
    }

    public PointsStatisticsVO(Long totalUsers, Integer totalEarned, Integer totalSpent, Integer totalExchanges) {
        this.totalUsers = totalUsers;
        this.totalEarned = totalEarned;
        this.totalSpent = totalSpent;
        this.totalExchanges = totalExchanges;
    }

    public Long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(Long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public Integer getTotalEarned() {
        return totalEarned;
    }

    public void setTotalEarned(Integer totalEarned) {
        this.totalEarned = totalEarned;
    }

    public Integer getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(Integer totalSpent) {
        this.totalSpent = totalSpent;
    }

    public Integer getTotalExchanges() {
        return totalExchanges;
    }

    public void setTotalExchanges(Integer totalExchanges) {
        this.totalExchanges = totalExchanges;
    }
}
