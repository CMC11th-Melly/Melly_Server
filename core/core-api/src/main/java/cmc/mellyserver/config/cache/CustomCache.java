package cmc.mellyserver.config.cache;

import java.util.concurrent.Callable;

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

    /*
    1. 로컬 캐시를 조회
    2. 로컬 캐시에 데이터 없으면 글로벌 캐시 조회 하고, 로컬 캐시 업데이트
    3. 만약 글로벌 캐시에도 없으면
     */
    @Override
    public ValueWrapper get(Object key) {
        return circuitBreaker.run(() -> globalCache.get(key), ((throwable) -> fallback()));
    }

    /*
    Cache가 ValueWrapper로 null을 반환하면 캐싱된 데이터가 없다고 판단 후, 실제 로직을 통해 DB 쿼리를 진행합니다.
     */
    private ValueWrapper fallback() {
        log.error("글로벌 캐시 다운, Fallback 메서드 실행");
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
            log.error("글로벌 캐시 다운, 캐시 갱신 실패");
        }
    }

    @Override
    public void evict(Object key) {
        try {
            globalCache.evict(key);
        } catch (QueryTimeoutException e) {
            log.error("글로벌 캐시 다운, 캐시 삭제 실패");
        }
    }

    @Override
    public void clear() {
        try {
            globalCache.clear();
        } catch (QueryTimeoutException e) {
            log.error("글로벌 캐시 다운, 캐시 초기화 실패");
        }
    }
}
