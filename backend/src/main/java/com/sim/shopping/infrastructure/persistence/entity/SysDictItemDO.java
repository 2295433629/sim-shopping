package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * SysDictItem数据实体，对应数据库表
 *
 * @author Sim Team
 * @since 1.0.0
 */
@TableName("t_sys_dict_item")
public class SysDictItemDO extends BaseEntity {
    private String label;
    private Long dictTypeId;
    private Integer sortOrder;
    private String status;
    private String value;

    /**
     * 获取Label
     * @return 返回结果
     */
    public String getLabel() { return this.label; }
    /**
     * set Label
     * @param label label
     */
    public void setLabel(String label) { this.label = label; }
    /**
     * 获取Dict Type Id
     * @return 返回结果
     */
    public Long getDictTypeId() { return this.dictTypeId; }
    /**
     * set Dict Type Id
     * @param dictTypeId dictTypeId
     */
    public void setDictTypeId(Long dictTypeId) { this.dictTypeId = dictTypeId; }
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
     * 获取Status
     * @return 返回结果
     */
    public String getStatus() { return this.status; }
    /**
     * set Status
     * @param status status
     */
    public void setStatus(String status) { this.status = status; }
    /** 获取Value */
    public String getValue() { return this.value; }
    /** set Value */
    public void setValue(String value) { this.value = value; }
}
