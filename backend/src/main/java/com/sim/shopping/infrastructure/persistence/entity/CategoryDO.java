package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * 分类实体，对应 t_category 表，存储商品分类信息
 *
 * @author Sim Team
 * @since 1.0.0
 */
@TableName("t_category")
public class CategoryDO extends BaseEntity {
    private Long parentId;
    private String name;
    private Integer level;
    private Integer sortOrder;
    private String icon;
    private String status;

    /**
     * 获取Parent Id
     * @return 返回结果
     */
    public Long getParentId() { return this.parentId; }
    /**
     * set Parent Id
     * @param parentId parentId
     */
    public void setParentId(Long parentId) { this.parentId = parentId; }
    /** 获取Name */
    public String getName() { return this.name; }
    /** set Name */
    public void setName(String name) { this.name = name; }
    /**
     * 获取Level
     * @return 返回结果
     */
    public Integer getLevel() { return this.level; }
    /**
     * set Level
     * @param level level
     */
    public void setLevel(Integer level) { this.level = level; }
    /**
     * 获取Sort Order
     * @return 返回结果
     */
    public Integer getSortOrder() { return this.sortOrder; }
    /**
     * set Sort Order
     * @param sortOrder sortOrder
     */
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    /**
     * 获取Icon
     * @return 返回结果
     */
    public String getIcon() { return this.icon; }
    /**
     * set Icon
     * @param icon icon
     */
    public void setIcon(String icon) { this.icon = icon; }
    /**
     * 获取Status
     * @return 返回结果
     */
    public String getStatus() { return this.status; }
    /**
     * set Status
     * @param status status
     */
    public void setStatus(String status) { this.status = status; }
}
