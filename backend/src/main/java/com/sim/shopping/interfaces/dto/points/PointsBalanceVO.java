package com.sim.shopping.interfaces.dto.points;

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

    public Integer getCurrentPoints() {
        return currentPoints;
    }

    public void setCurrentPoints(Integer currentPoints) {
        this.currentPoints = currentPoints;
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
}
