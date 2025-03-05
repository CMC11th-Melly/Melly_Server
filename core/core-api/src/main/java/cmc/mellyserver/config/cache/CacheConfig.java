package cmc.mellyserver.config.cache;

import static cmc.mellyserver.config.circuitbreaker.CircuitBreakerConstants.*;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@EnableCaching
@Configuration
public class CacheConfig {

    private static final Duration COMMAND_TIMEOUT = Duration.ofMillis(300);

    @Value("${spring.redis.cache.host}")
    private String cacheHost;

    @Value("${spring.redis.cache.port}")
    private int cachePort;

    @Bean(name = "redisCacheConnectionFactory")
    RedisConnectionFactory redisCacheConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(cacheHost);
        redisStandaloneConfiguration.setPort(cachePort);
        LettuceClientConfiguration lettuceClientConfiguration = LettuceClientConfiguration.builder()
            .commandTimeout(COMMAND_TIMEOUT).build();
        return new LettuceConnectionFactory(redisStandaloneConfiguration, lettuceClientConfiguration);
    }

    @Bean
    public ObjectMapper objectMapper() {

        BasicPolymorphicTypeValidator typeValidator = BasicPolymorphicTypeValidator
            .builder()
            .allowIfSubType(Object.class)
            .build();

        return new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .activateDefaultTyping(typeValidator, ObjectMapper.DefaultTyping.NON_FINAL)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Bean
    public CacheManager customCacheManager(CircuitBreakerFactory circuitBreakerFactory) {

        /* Serializer 설정 */
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                new GenericJackson2JsonRedisSerializer(objectMapper())));

        /* Cache TTL 설정 */
        Map<String, RedisCacheConfiguration> redisCacheConfigMap = new ConcurrentHashMap<>();
        redisCacheConfigMap.put(CacheNames.USER, defaultConfig.entryTtl(Duration.ofHours(1))); // 코드 상으로 직접 eviction 가능
        redisCacheConfigMap.put(CacheNames.SCRAP, defaultConfig.entryTtl(Duration.ofHours(1))); // 코드 상으로 직접 eviction 가능
        redisCacheConfigMap.put(CacheNames.MEMORY, defaultConfig.entryTtl(Duration.ofMinutes(5)));
        redisCacheConfigMap.put(CacheNames.GROUP, defaultConfig.entryTtl(Duration.ofMinutes(5)));

        RedisCacheManager redisCacheManager = RedisCacheManager.builder(redisCacheConnectionFactory())
            .withInitialCacheConfigurations(redisCacheConfigMap).build();

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create(CACHE_CIRCUIT);
        return new CustomCacheManager(redisCacheManager, circuitBreaker);
    }
}
