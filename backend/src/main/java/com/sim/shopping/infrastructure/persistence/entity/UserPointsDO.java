package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * UserPoints数据实体，对应数据库表
 *
 * @author Sim Team
 * @since 1.0.0
 */
@TableName("t_user_points")
public class UserPointsDO extends BaseEntity {

    @TableField("user_id")
    private Long userId;

    @TableField("total_points")
    private Integer totalPoints;

    @TableField("available_points")
    private Integer availablePoints;

    /**
     * 获取User Id
     * @return 返回结果
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * set User Id
     * @param userId userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
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
     * 获取Available Points
     * @return 返回结果
     */
    public Integer getAvailablePoints() {
        return availablePoints;
    }

    /**
     * set Available Points
     * @param availablePoints availablePoints
     */
    public void setAvailablePoints(Integer availablePoints) {
        this.availablePoints = availablePoints;
    }
}
