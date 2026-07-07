package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@TableName("t_category")
public class CategoryDO extends BaseEntity {
    private Long parentId;
    private String name;
    private Integer level;
    private Integer sortOrder;
    private String icon;
    private String status;

    public Long getParentId() { return this.parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }
    public Integer getLevel() { return this.level; }
    public void setLevel(Integer level) { this.level = level; }
    public Integer getSortOrder() { return this.sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public String getIcon() { return this.icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }
}
