package com.sim.shopping.interfaces.dto.signin;

/**
 * SignInResult
 *
 * @author Sim Team
 * @since 1.0.0
 */
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

    /**
     * 获取Points
     * @return 返回结果
     */
    public Integer getPoints() { return this.points; }
    /**
     * set Points
     * @param points points
     */
    public void setPoints(Integer points) { this.points = points; }
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
     * 获取Signed
     * @return 返回结果
     */
    public Boolean getSigned() { return this.signed; }
    /**
     * set Signed
     * @param signed signed
     */
    public void setSigned(Boolean signed) { this.signed = signed; }
}
