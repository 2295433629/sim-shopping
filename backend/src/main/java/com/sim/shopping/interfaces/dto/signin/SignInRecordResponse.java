package com.sim.shopping.interfaces.dto.signin;

import java.time.LocalDate;

public class SignInRecordResponse {
    private Long id;
    private LocalDate signDate;
    private Integer consecutiveDays;
    private Integer pointsEarned;

    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getSignDate() { return this.signDate; }
    public void setSignDate(LocalDate signDate) { this.signDate = signDate; }
    public Integer getConsecutiveDays() { return this.consecutiveDays; }
    public void setConsecutiveDays(Integer consecutiveDays) { this.consecutiveDays = consecutiveDays; }
    public Integer getPointsEarned() { return this.pointsEarned; }
    public void setPointsEarned(Integer pointsEarned) { this.pointsEarned = pointsEarned; }
}
