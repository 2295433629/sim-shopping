package com.sim.shopping.interfaces.dto.system;

import java.time.LocalDateTime;

public class DictTypeResponse {

    private Long id;
    private String dictName;
    private String dictCode;
    private String status;
    private LocalDateTime createdAt;

    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }
    public String getDictName() { return this.dictName; }
    public void setDictName(String dictName) { this.dictName = dictName; }
    public String getDictCode() { return this.dictCode; }
    public void setDictCode(String dictCode) { this.dictCode = dictCode; }
    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return this.createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
