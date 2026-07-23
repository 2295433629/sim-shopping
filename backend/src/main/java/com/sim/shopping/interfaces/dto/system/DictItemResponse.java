package com.sim.shopping.interfaces.dto.system;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DictItem响应对象，封装接口出参
 * 字段名与前端 DictItem 接口对齐
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class DictItemResponse {

    private Long id;
    private Long dictTypeId;

    @JsonProperty("itemLabel")
    private String itemText;

    @JsonProperty("itemValue")
    private String itemValue;

    @JsonProperty("sort")
    private Integer sortOrder;

    @JsonProperty("status")
    private Integer status;

    /** 获取Id */
    public Long getId() { return this.id; }
    /** set Id */
    public void setId(Long id) { this.id = id; }
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
     * 获取Item Text
     * @return 返回结果
     */
    public String getItemText() { return this.itemText; }
    /**
     * set Item Text
     * @param itemText itemText
     */
    public void setItemText(String itemText) { this.itemText = itemText; }
    /**
     * 获取Item Value
     * @return 返回结果
     */
    public String getItemValue() { return this.itemValue; }
    /**
     * set Item Value
     * @param itemValue itemValue
     */
    public void setItemValue(String itemValue) { this.itemValue = itemValue; }
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
    public Integer getStatus() { return this.status; }
    /**
     * set Status
     * @param status status
     */
    public void setStatus(Integer status) { this.status = status; }
}
