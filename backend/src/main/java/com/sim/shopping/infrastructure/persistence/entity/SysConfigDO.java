package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@TableName("t_sys_config")
public class SysConfigDO extends BaseEntity {
    private String description;
    private String configName;
    private String configKey;
    private String configType;
    private String module;
    private Integer isSystem;
    private String configValue;

    public String getDescription() { return this.description; }
    public void setDescription(String description) { this.description = description; }
    public String getConfigName() { return this.configName; }
    public void setConfigName(String configName) { this.configName = configName; }
    public String getConfigKey() { return this.configKey; }
    public void setConfigKey(String configKey) { this.configKey = configKey; }
    public String getConfigType() { return this.configType; }
    public void setConfigType(String configType) { this.configType = configType; }
    public String getModule() { return this.module; }
    public void setModule(String module) { this.module = module; }
    public Integer getIsSystem() { return this.isSystem; }
    public void setIsSystem(Integer isSystem) { this.isSystem = isSystem; }
    public String getConfigValue() { return this.configValue; }
    public void setConfigValue(String configValue) { this.configValue = configValue; }
}
