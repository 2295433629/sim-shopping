package com.sim.shopping.interfaces.dto.points;

/**
 * PointsStatistics视图对象，用于前端展示
 *
 * @author Sim Team
 * @since 1.0.0
 */
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

    /**
     * 获取Total Users
     * @return 返回结果
     */
    public Long getTotalUsers() {
        return totalUsers;
    }

    /**
     * set Total Users
     * @param totalUsers totalUsers
     */
    public void setTotalUsers(Long totalUsers) {
        this.totalUsers = totalUsers;
    }

    /**
     * 获取Total Earned
     * @return 返回结果
     */
    public Integer getTotalEarned() {
        return totalEarned;
    }

    /**
     * set Total Earned
     * @param totalEarned totalEarned
     */
    public void setTotalEarned(Integer totalEarned) {
        this.totalEarned = totalEarned;
    }

    /**
     * 获取Total Spent
     * @return 返回结果
     */
    public Integer getTotalSpent() {
        return totalSpent;
    }

    /**
     * set Total Spent
     * @param totalSpent totalSpent
     */
    public void setTotalSpent(Integer totalSpent) {
        this.totalSpent = totalSpent;
    }

    /**
     * 获取Total Exchanges
     * @return 返回结果
     */
    public Integer getTotalExchanges() {
        return totalExchanges;
    }

    /**
     * set Total Exchanges
     * @param totalExchanges totalExchanges
     */
    public void setTotalExchanges(Integer totalExchanges) {
        this.totalExchanges = totalExchanges;
    }
}
