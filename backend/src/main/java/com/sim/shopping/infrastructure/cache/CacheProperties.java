package com.sim.shopping.infrastructure.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.cache")
public class CacheProperties {

    /** redis / database */
    private String type = "redis";

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
