package cmc.mellyserver.config.cache;

import java.util.Collection;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.data.redis.cache.RedisCacheManager;

public class CustomCacheManager implements CacheManager {

    private final RedisCacheManager globalCacheManager;
    private final CircuitBreaker circuitBreaker;

    public CustomCacheManager(RedisCacheManager globalCacheManager,
        CircuitBreaker circuitBreaker) {
        this.globalCacheManager = globalCacheManager;
        this.circuitBreaker = circuitBreaker;
    }

    @Override
    public Cache getCache(String name) {
        return new CustomCache(globalCacheManager.getCache(name), circuitBreaker);
    }

    @Override
    public Collection<String> getCacheNames() {
        return globalCacheManager.getCacheNames();
    }
}
