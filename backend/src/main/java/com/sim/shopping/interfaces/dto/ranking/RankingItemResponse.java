package com.sim.shopping.interfaces.dto.ranking;

import java.math.BigDecimal;

/**
 * RankingItem响应对象，封装接口出参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class RankingItemResponse {

    private int rank;
    private Long userId;
    private String nickname;
    private String avatar;
    private BigDecimal value;
    private String unit;

    /**
     * 获取Rank
     * @return 返回结果
     */
    public int getRank() {
        return this.rank;
    }

    /**
     * set Rank
     * @param rank rank
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * 获取User Id
     * @return 返回结果
     */
    public Long getUserId() {
        return this.userId;
    }

    /**
     * set User Id
     * @param userId userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 获取Nickname
     * @return 返回结果
     */
    public String getNickname() {
        return this.nickname;
    }

    /**
     * set Nickname
     * @param nickname nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 获取Avatar
     * @return 返回结果
     */
    public String getAvatar() {
        return this.avatar;
    }

    /**
     * set Avatar
     * @param avatar avatar
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /** 获取Value */
    public BigDecimal getValue() {
        return this.value;
    }

    /** set Value */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    /**
     * 获取Unit
     * @return 返回结果
     */
    public String getUnit() {
        return this.unit;
    }

    /**
     * set Unit
     * @param unit unit
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }
}
