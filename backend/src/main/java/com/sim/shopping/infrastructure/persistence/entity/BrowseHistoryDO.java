package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * BrowseHistory数据实体，对应数据库表
 *
 * @author Sim Team
 * @since 1.0.0
 */
@TableName("t_browse_history")
public class BrowseHistoryDO extends BaseEntity {
    private Long productId;
    private LocalDateTime viewedAt;
    private Long userId;

    /**
     * 获取Product Id
     * @return 返回结果
     */
    public Long getProductId() { return this.productId; }
    /**
     * set Product Id
     * @param productId productId
     */
    public void setProductId(Long productId) { this.productId = productId; }
    /**
     * 获取Viewed At
     * @return 返回结果
     */
    public LocalDateTime getViewedAt() { return this.viewedAt; }
    /**
     * set Viewed At
     * @param viewedAt viewedAt
     */
    public void setViewedAt(LocalDateTime viewedAt) { this.viewedAt = viewedAt; }
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
}
