package com.sim.shopping.interfaces.dto.system;

import jakarta.validation.constraints.NotBlank;

public class DictItemCreateRequest {

    @NotBlank(message = "字典项文本不能为空")
    private String itemText;

    @NotBlank(message = "字典项值不能为空")
    private String itemValue;

    private Integer sortOrder;
    private String status;

    public String getItemText() { return this.itemText; }
    public void setItemText(String itemText) { this.itemText = itemText; }
    public String getItemValue() { return this.itemValue; }
    public void setItemValue(String itemValue) { this.itemValue = itemValue; }
    public Integer getSortOrder() { return this.sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }
}
