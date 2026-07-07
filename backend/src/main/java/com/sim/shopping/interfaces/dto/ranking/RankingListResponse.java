package com.sim.shopping.interfaces.dto.ranking;

import java.util.List;

public class RankingListResponse {

    private String period;
    private List<RankingItemResponse> list;

    public String getPeriod() {
        return this.period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public List<RankingItemResponse> getList() {
        return this.list;
    }

    public void setList(List<RankingItemResponse> list) {
        this.list = list;
    }
}
