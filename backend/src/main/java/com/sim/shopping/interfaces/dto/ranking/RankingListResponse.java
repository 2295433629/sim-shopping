package com.sim.shopping.interfaces.dto.ranking;

import java.util.List;

/**
 * RankingList响应对象，封装接口出参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class RankingListResponse {

    private String period;
    private List<RankingItemResponse> list;

    /**
     * 获取Period
     * @return 返回结果
     */
    public String getPeriod() {
        return this.period;
    }

    /**
     * set Period
     * @param period period
     */
    public void setPeriod(String period) {
        this.period = period;
    }

    /**
     * 查询列表
     * @return 返回结果
     */
    public List<RankingItemResponse> getList() {
        return this.list;
    }

    /**
     * set List
     * @param list list
     */
    public void setList(List<RankingItemResponse> list) {
        this.list = list;
    }
}
