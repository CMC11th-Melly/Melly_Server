package cmc.mellyserver.config.cache;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.springframework.cache.Cache;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
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

        return Decorators
            .ofSupplier(flightsSupplier)
            .withCircuitBreaker(circuitBreaker)
            .withFallback((e) -> fallback())
            .decorate().get();

    }

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
        Consumer consumer = (k) -> globalCache.put(key, value);
        Decorators
            .ofConsumer(consumer)
            .withCircuitBreaker(circuitBreaker)
            .decorate();
    }

    @Override
    public void evict(Object key) {
        Consumer consumer = (k) -> globalCache.evict(key);
        Decorators
            .ofConsumer(consumer)
            .withCircuitBreaker(circuitBreaker)
            .decorate();
    }

    @Override
    public void clear() {
        Consumer consumer = (k) -> globalCache.clear();
        Decorators
            .ofConsumer(consumer)
            .withCircuitBreaker(circuitBreaker)
            .decorate();
    }
}
