package com.sim.shopping.infrastructure.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CacheProviderFactory {

    private static final Logger log = LoggerFactory.getLogger(CacheProviderFactory.class);

    private final CacheProperties cacheProperties;
    private final RedisCacheProvider redisCacheProvider;
    private final DatabaseCacheProvider databaseCacheProvider;

    public CacheProviderFactory(CacheProperties cacheProperties,
                                RedisCacheProvider redisCacheProvider,
                                DatabaseCacheProvider databaseCacheProvider) {
        this.cacheProperties = cacheProperties;
        this.redisCacheProvider = redisCacheProvider;
        this.databaseCacheProvider = databaseCacheProvider;
        log.info("CacheProviderFactory initialized, cache.type={}", cacheProperties.getType());
    }

    public CacheProvider getCacheProvider() {
        if ("database".equalsIgnoreCase(cacheProperties.getType())) {
            return databaseCacheProvider;
        }
        return redisCacheProvider;
    }
}
