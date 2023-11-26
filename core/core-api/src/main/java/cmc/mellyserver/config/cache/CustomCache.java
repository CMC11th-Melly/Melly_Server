package cmc.mellyserver.config.cache;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

import org.springframework.cache.Cache;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.dao.QueryTimeoutException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomCache implements Cache {

    private final Cache globalCache;

    private final CircuitBreaker circuitBreaker;

    public CustomCache(Cache globalCache, CircuitBreaker circuitBreaker) {
        this.globalCache = globalCache;
        this.circuitBreaker = circuitBreaker;

    }

    @Override
    public String getName() {
        return globalCache.getName();
    }

    @Override
    public Object getNativeCache() {
        return globalCache.getNativeCache();
    }

    @Override
    public ValueWrapper get(Object key) {
        Supplier<ValueWrapper> flightsSupplier = () -> (globalCache.get(key));
        return circuitBreaker.run(flightsSupplier, (throwable -> fallback()));
    }

    /*
    Cache가 ValueWrapper로 null을 반환하면 캐싱된 데이터가 없다고 판단 후, 실제 로직을 통해 DB 쿼리를 진행합니다.
     */
    private ValueWrapper fallback() {
        log.error("global cache server down, fallback method start");
        return null;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        return globalCache.get(key, type);
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        return globalCache.get(key, valueLoader);
    }

    @Override
    public void put(Object key, Object value) {

        try {
            globalCache.put(key, value);
        } catch (QueryTimeoutException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void evict(Object key) {
        globalCache.evict(key);
    }

    @Override
    public void clear() {
        globalCache.clear();
    }
}
