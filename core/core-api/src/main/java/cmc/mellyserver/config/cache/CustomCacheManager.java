package cmc.mellyserver.config.cache;

import java.util.Collection;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;

public class CustomCacheManager implements CacheManager {

    private final CacheManager delegate;
    private final CircuitBreaker circuitBreaker;

    public CustomCacheManager(CacheManager delegate, CircuitBreaker circuitBreaker) {
        this.delegate = delegate;
        this.circuitBreaker = circuitBreaker;
    }

    @Override
    public Cache getCache(String name) {
        return new CustomCache(delegate.getCache(name), circuitBreaker);
    }

    @Override
    public Collection<String> getCacheNames() {
        return delegate.getCacheNames();
    }
}
