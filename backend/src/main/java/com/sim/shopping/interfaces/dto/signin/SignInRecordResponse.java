package com.sim.shopping.interfaces.dto.signin;

import java.time.LocalDate;

/**
 * SignInRecord响应对象，封装接口出参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class SignInRecordResponse {
    private Long id;
    private LocalDate signDate;
    private Integer consecutiveDays;
    private Integer pointsEarned;

    /** 获取Id */
    public Long getId() { return this.id; }
    /** set Id */
    public void setId(Long id) { this.id = id; }
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
     * 获取Points Earned
     * @return 返回结果
     */
    public Integer getPointsEarned() { return this.pointsEarned; }
    /**
     * set Points Earned
     * @param pointsEarned pointsEarned
     */
    public void setPointsEarned(Integer pointsEarned) { this.pointsEarned = pointsEarned; }
}
