package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * 签到记录实体，对应 t_sign_in_record 表，存储用户签到历史
 *
 * @author Sim Team
 * @since 1.0.0
 */
@TableName("t_sign_in_record")
public class SignInRecordDO extends BaseEntity {
    private Integer pointsEarned;
    private Long userId;
    private Integer consecutiveDays;
    private LocalDate signDate;

    /**
     * 获取Points Earned
     * @return 返回结果
     */
    public Integer getPointsEarned() { return this.pointsEarned; }
    /**
     * set Points Earned
     * @param pointsEarned pointsEarned
     */
    public void setPointsEarned(Integer pointsEarned) { this.pointsEarned = pointsEarned; }
    /**
     * 获取User Id
     * @return 返回结果
     */
    public Long getUserId() { return this.userId; }
    /**
     * set User Id
     * @param userId userId
     */
    public void setUserId(Long userId) { this.userId = userId; }
    /**
     * 获取连续签到天数
     * @return 返回结果
     */
    public Integer getConsecutiveDays() { return this.consecutiveDays; }
    /**
     * set Consecutive Days
     * @param consecutiveDays consecutiveDays
     */
    public void setConsecutiveDays(Integer consecutiveDays) { this.consecutiveDays = consecutiveDays; }
    /**
     * 获取Sign Date
     * @return 返回结果
     */
    public LocalDate getSignDate() { return this.signDate; }
    /**
     * set Sign Date
     * @param signDate signDate
     */
    public void setSignDate(LocalDate signDate) { this.signDate = signDate; }
}
