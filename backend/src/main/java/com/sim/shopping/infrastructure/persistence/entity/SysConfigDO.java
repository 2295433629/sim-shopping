package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * SysConfig数据实体，对应数据库表
 *
 * @author Sim Team
 * @since 1.0.0
 */
@TableName("t_sys_config")
public class SysConfigDO extends BaseEntity {
    private String description;
    private String configName;
    private String configKey;
    private String configType;
    private String module;
    private Integer isSystem;
    private String configValue;

    /**
     * 获取Description
     * @return 返回结果
     */
    public String getDescription() { return this.description; }
    /**
     * set Description
     * @param description description
     */
    public void setDescription(String description) { this.description = description; }
    /**
     * 获取Config Name
     * @return 返回结果
     */
    public String getConfigName() { return this.configName; }
    /**
     * set Config Name
     * @param configName configName
     */
    public void setConfigName(String configName) { this.configName = configName; }
    /**
     * 获取Config Key
     * @return 返回结果
     */
    public String getConfigKey() { return this.configKey; }
    /**
     * set Config Key
     * @param configKey configKey
     */
    public void setConfigKey(String configKey) { this.configKey = configKey; }
    /**
     * 获取Config Type
     * @return 返回结果
     */
    public String getConfigType() { return this.configType; }
    /**
     * set Config Type
     * @param configType configType
     */
    public void setConfigType(String configType) { this.configType = configType; }
    /**
     * 获取Module
     * @return 返回结果
     */
    public String getModule() { return this.module; }
    /**
     * set Module
     * @param module module
     */
    public void setModule(String module) { this.module = module; }
    /**
     * 获取Is System
     * @return 返回结果
     */
    public Integer getIsSystem() { return this.isSystem; }
    /**
     * set Is System
     * @param isSystem isSystem
     */
    public void setIsSystem(Integer isSystem) { this.isSystem = isSystem; }
    /**
     * 获取Config Value
     * @return 返回结果
     */
    public String getConfigValue() { return this.configValue; }
    /**
     * set Config Value
     * @param configValue configValue
     */
    public void setConfigValue(String configValue) { this.configValue = configValue; }
}
