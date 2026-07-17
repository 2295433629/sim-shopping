package com.sim.shopping.interfaces.dto.system;

import jakarta.validation.constraints.NotBlank;

/**
 * DictType请求对象，封装接口入参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class DictTypeRequest {

    @NotBlank(message = "字典名称不能为空")
    private String dictName;

    @NotBlank(message = "字典编码不能为空")
    private String dictCode;

    private String status;

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
}
