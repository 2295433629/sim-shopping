package com.sim.shopping.interfaces.dto.points;

/**
 * PointsBalance视图对象，用于前端展示
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class PointsBalanceVO {

    private Integer currentPoints;
    private Integer totalEarned;
    private Integer totalSpent;

    public PointsBalanceVO() {
    }

    public PointsBalanceVO(Integer currentPoints, Integer totalEarned, Integer totalSpent) {
        this.currentPoints = currentPoints;
        this.totalEarned = totalEarned;
        this.totalSpent = totalSpent;
    }

    /**
     * 获取Current Points
     * @return 返回结果
     */
    public Integer getCurrentPoints() {
        return currentPoints;
    }

    /**
     * set Current Points
     * @param currentPoints currentPoints
     */
    public void setCurrentPoints(Integer currentPoints) {
        this.currentPoints = currentPoints;
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
}
