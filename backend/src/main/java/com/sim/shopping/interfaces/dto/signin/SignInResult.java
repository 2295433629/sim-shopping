package com.sim.shopping.interfaces.dto.signin;

public class SignInResult {
    private Integer points;
    private Integer consecutiveDays;
    private Boolean signed;

    public SignInResult() {
    }

    public SignInResult(Integer points, Integer consecutiveDays, Boolean signed) {
        this.points = points;
        this.consecutiveDays = consecutiveDays;
        this.signed = signed;
    }

    public Integer getPoints() { return this.points; }
    public void setPoints(Integer points) { this.points = points; }
    public Integer getConsecutiveDays() { return this.consecutiveDays; }
    public void setConsecutiveDays(Integer consecutiveDays) { this.consecutiveDays = consecutiveDays; }
    public Boolean getSigned() { return this.signed; }
    public void setSigned(Boolean signed) { this.signed = signed; }
}
