package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@TableName("t_sys_dict_type")
public class SysDictTypeDO extends BaseEntity {
    private String remark;
    private String status;
    private String dictCode;
    private String dictName;

    public String getRemark() { return this.remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }
    public String getDictCode() { return this.dictCode; }
    public void setDictCode(String dictCode) { this.dictCode = dictCode; }
    public String getDictName() { return this.dictName; }
    public void setDictName(String dictName) { this.dictName = dictName; }
}
