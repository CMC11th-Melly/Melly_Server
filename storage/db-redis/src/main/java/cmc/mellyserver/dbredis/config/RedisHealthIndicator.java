package cmc.mellyserver.dbredis.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

@Component
public class RedisHealthIndicator implements HealthIndicator {

    private final RedisConnectionFactory redisCacheConnectionFactory;

    public RedisHealthIndicator(
        @Qualifier("redisCacheConnectionFactory") RedisConnectionFactory redisCacheConnectionFactory) {
        this.redisCacheConnectionFactory = redisCacheConnectionFactory;
    }

    @Override
    public Health health() {
        return null;
    }
}
