package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * SysDictType数据实体，对应数据库表
 *
 * @author Sim Team
 * @since 1.0.0
 */
@TableName("t_sys_dict_type")
public class SysDictTypeDO extends BaseEntity {
    private String remark;
    private String status;
    private String dictCode;
    private String dictName;

    /**
     * 获取Remark
     * @return 返回结果
     */
    public String getRemark() { return this.remark; }
    /**
     * set Remark
     * @param remark remark
     */
    public void setRemark(String remark) { this.remark = remark; }
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
    /**
     * 获取Dict Code
     * @return 返回结果
     */
    public String getDictCode() { return this.dictCode; }
    /**
     * set Dict Code
     * @param dictCode dictCode
     */
    public void setDictCode(String dictCode) { this.dictCode = dictCode; }
    /**
     * 获取Dict Name
     * @return 返回结果
     */
    public String getDictName() { return this.dictName; }
    /**
     * set Dict Name
     * @param dictName dictName
     */
    public void setDictName(String dictName) { this.dictName = dictName; }
}
