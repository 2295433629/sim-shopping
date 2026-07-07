package com.sim.shopping.interfaces.dto.system;

import jakarta.validation.constraints.NotBlank;

public class DictTypeRequest {

    @NotBlank(message = "字典名称不能为空")
    private String dictName;

    @NotBlank(message = "字典编码不能为空")
    private String dictCode;

    private String status;

    public String getDictName() { return this.dictName; }
    public void setDictName(String dictName) { this.dictName = dictName; }
    public String getDictCode() { return this.dictCode; }
    public void setDictCode(String dictCode) { this.dictCode = dictCode; }
    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }
}
