package com.sim.shopping.interfaces.dto.system;

import java.time.LocalDateTime;

/**
 * DictType响应对象，封装接口出参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class DictTypeResponse {

    private Long id;
    private String dictName;
    private String dictCode;
    private String status;
    private LocalDateTime createdAt;

    /** 获取Id */
    public Long getId() { return this.id; }
    /** set Id */
    public void setId(Long id) { this.id = id; }
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
     * 获取Status
     * @return 返回结果
     */
    public String getStatus() { return this.status; }
    /**
     * set Status
     * @param status status
     */
    public void setStatus(String status) { this.status = status; }
    /** 获取Created At */
    public LocalDateTime getCreatedAt() { return this.createdAt; }
    /** set Created At */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
