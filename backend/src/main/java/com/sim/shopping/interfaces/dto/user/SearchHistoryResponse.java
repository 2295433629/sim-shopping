package com.sim.shopping.interfaces.dto.user;

import java.time.LocalDateTime;

/**
 * SearchHistory响应对象，封装接口出参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class SearchHistoryResponse {

    private Long id;
    private String keyword;
    private LocalDateTime searchedAt;

    /** 获取Id */
    public Long getId() { return this.id; }
    /** set Id */
    public void setId(Long id) { this.id = id; }
    /**
     * 获取Keyword
     * @return 返回结果
     */
    public String getKeyword() { return this.keyword; }
    /**
     * set Keyword
     * @param keyword keyword
     */
    public void setKeyword(String keyword) { this.keyword = keyword; }
    /**
     * 获取Searched At
     * @return 返回结果
     */
    public LocalDateTime getSearchedAt() { return this.searchedAt; }
    /**
     * set Searched At
     * @param searchedAt searchedAt
     */
    public void setSearchedAt(LocalDateTime searchedAt) { this.searchedAt = searchedAt; }
}
