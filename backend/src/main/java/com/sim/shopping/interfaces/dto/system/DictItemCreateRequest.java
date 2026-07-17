package com.sim.shopping.interfaces.dto.system;

import jakarta.validation.constraints.NotBlank;

/**
 * DictItemCreate请求对象，封装接口入参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class DictItemCreateRequest {

    @NotBlank(message = "字典项文本不能为空")
    private String itemText;

    @NotBlank(message = "字典项值不能为空")
    private String itemValue;

    private Integer sortOrder;
    private String status;

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
    public String getStatus() { return this.status; }
    /**
     * set Status
     * @param status status
     */
    public void setStatus(String status) { this.status = status; }
}
