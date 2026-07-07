package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@TableName("t_sys_dict_item")
public class SysDictItemDO extends BaseEntity {
    private String label;
    private Long dictTypeId;
    private Integer sortOrder;
    private String status;
    private String value;

    public String getLabel() { return this.label; }
    public void setLabel(String label) { this.label = label; }
    public Long getDictTypeId() { return this.dictTypeId; }
    public void setDictTypeId(Long dictTypeId) { this.dictTypeId = dictTypeId; }
    public Integer getSortOrder() { return this.sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }
    public String getValue() { return this.value; }
    public void setValue(String value) { this.value = value; }
}
