package com.sim.shopping.interfaces.dto.user;

import java.time.LocalDateTime;

public class SearchHistoryResponse {

    private Long id;
    private String keyword;
    private LocalDateTime searchedAt;

    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }
    public String getKeyword() { return this.keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
    public LocalDateTime getSearchedAt() { return this.searchedAt; }
    public void setSearchedAt(LocalDateTime searchedAt) { this.searchedAt = searchedAt; }
}
