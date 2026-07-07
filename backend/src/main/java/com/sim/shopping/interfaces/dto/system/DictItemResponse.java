package com.sim.shopping.interfaces.dto.system;

public class DictItemResponse {

    private Long id;
    private Long dictTypeId;
    private String itemText;
    private String itemValue;
    private Integer sortOrder;
    private String status;

    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }
    public Long getDictTypeId() { return this.dictTypeId; }
    public void setDictTypeId(Long dictTypeId) { this.dictTypeId = dictTypeId; }
    public String getItemText() { return this.itemText; }
    public void setItemText(String itemText) { this.itemText = itemText; }
    public String getItemValue() { return this.itemValue; }
    public void setItemValue(String itemValue) { this.itemValue = itemValue; }
    public Integer getSortOrder() { return this.sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }
}
