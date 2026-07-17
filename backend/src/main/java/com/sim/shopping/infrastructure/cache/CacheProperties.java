package com.sim.shopping.infrastructure.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * CacheProperties
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Component
@ConfigurationProperties(prefix = "app.cache")
public class CacheProperties {

    /** redis / database */
    private String type = "redis";

    /** 获取Type */
    public String getType() { return type; }
    /** set Type */
    public void setType(String type) { this.type = type; }
}
