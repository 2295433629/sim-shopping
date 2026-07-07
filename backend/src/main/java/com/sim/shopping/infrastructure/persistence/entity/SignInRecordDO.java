package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@TableName("t_sign_in_record")
public class SignInRecordDO extends BaseEntity {
    private Integer pointsEarned;
    private Long userId;
    private Integer consecutiveDays;
    private LocalDate signDate;

    public Integer getPointsEarned() { return this.pointsEarned; }
    public void setPointsEarned(Integer pointsEarned) { this.pointsEarned = pointsEarned; }
    public Long getUserId() { return this.userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Integer getConsecutiveDays() { return this.consecutiveDays; }
    public void setConsecutiveDays(Integer consecutiveDays) { this.consecutiveDays = consecutiveDays; }
    public LocalDate getSignDate() { return this.signDate; }
    public void setSignDate(LocalDate signDate) { this.signDate = signDate; }
}
