package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@TableName("t_browse_history")
public class BrowseHistoryDO extends BaseEntity {
    private Long productId;
    private LocalDateTime viewedAt;
    private Long userId;

    public Long getProductId() { return this.productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public LocalDateTime getViewedAt() { return this.viewedAt; }
    public void setViewedAt(LocalDateTime viewedAt) { this.viewedAt = viewedAt; }
    public Long getUserId() { return this.userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
