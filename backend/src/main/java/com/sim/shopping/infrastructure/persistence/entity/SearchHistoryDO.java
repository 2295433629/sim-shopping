package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * SearchHistory数据实体，对应数据库表
 *
 * @author Sim Team
 * @since 1.0.0
 */
@TableName("t_search_history")
public class SearchHistoryDO extends BaseEntity {
    private String keyword;
    private Long userId;
    private LocalDateTime searchedAt;

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
     * 获取User Id
     * @return 返回结果
     */
    public Long getUserId() { return this.userId; }
    /**
     * set User Id
     * @param userId userId
     */
    public void setUserId(Long userId) { this.userId = userId; }
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
