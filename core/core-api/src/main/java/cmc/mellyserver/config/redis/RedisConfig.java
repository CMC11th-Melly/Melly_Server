package cmc.mellyserver.config.redis;

import static cmc.mellyserver.config.circuitbreaker.CircuitBreakerConstants.*;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import cmc.mellyserver.config.cache.CacheNames;
import cmc.mellyserver.config.cache.CustomCacheManager;

@EnableCaching
@Configuration
public class RedisConfig {

    @Value("${spring.redis.token.host}")
    private String tokenHost;

    @Value("${spring.redis.token.port}")
    private int tokenPort;

    @Value("${spring.redis.cache.host}")
    private String cacheHost;

    @Value("${spring.redis.cache.port}")
    private int cachePort;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(tokenHost);
        redisStandaloneConfiguration.setPort(tokenPort);

        LettuceClientConfiguration lettuceClientConfiguration = LettuceClientConfiguration.builder()
            .commandTimeout(Duration.ofMillis(200)).build();

        return new LettuceConnectionFactory(redisStandaloneConfiguration, lettuceClientConfiguration);
    }

    @Bean(name = "redisCacheConnectionFactory")
    RedisConnectionFactory redisCacheConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(cacheHost);
        redisStandaloneConfiguration.setPort(cachePort);

        LettuceClientConfiguration lettuceClientConfiguration = LettuceClientConfiguration.builder()
            .commandTimeout(Duration.ofMillis(200)).build();

        return new LettuceConnectionFactory(redisStandaloneConfiguration, lettuceClientConfiguration);
    }

    /*
    설정 종류
     - JavaTimeModule : 해당 모듈을 등록해줘야 Java 8의 date/time을 사용해서 string으로 직렬화 가능합니다.
     - SerializationFeature.WRITE_DATES_AS_TIMESTAMP : 해당 속성을 true로 설정하면 Long 타입으로 직렬화됩니다. 현재 Disable로 설정해서 String으로 직렬화되도록 설정했습니다.
     - DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES : 역직렬화시 클래스 변수에 매핑되지 않는 값이 있을때 예외를 발생시킬지 체크, 현재는 예외가 발생하지 않도록 false로 설정했습니다.
     */
    @Bean
    public ObjectMapper objectMapper() {

        return new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate() {

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public RedisMessageListenerContainer RedisMessageListener() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        return container;
    }

    /*
    캐시 TTL 시간 설정 기준
     - USER 프로필 캐시는 조회에 비해 변경 가능성이 적고 TTL로 1일을 설정했습니다.
     - SCRAP 캐시는 변경 가능성이 높지만 개인화된 데이터이고 TTL로 1일을 설정했습니다.
     - MEMORY 상세 페이지 캐시는 메모리 이외에도 그룹 정보, 장소 정보들이 혼합되기 때문에 TTL을 10분으로 짧게 설정했습니다.
     - GROUP 상세 페이지 캐시는 그룹 이외에도 유저 정보가 혼합되기 때문에 TTL을 10분으로 짧게 설정했습니다.

    Circuit breaker를 통한 HA 보장
    레디스로 분산 캐시를 구현 시 레디스 서버가 다운될 수 있습니다. 이때 Spring Cache는 자동으로 DB 조회를 하는 것이 아니라 예외가 발생합니다.
    이를 해결하기 위해 레디스 서버 장애시 fallback으로 DB 조회를 수행하고, 임계치 이상 예외 발생 시, Circuit을 Open하고 Redis 서버 조회 자체를
    막음으로써 Fail Fast를 통한 Redis Server Recovery를 유도했습니다.
     */
    @Bean
    public CacheManager customCacheManager(
        @Qualifier("redisCacheConnectionFactory") RedisConnectionFactory connectionFactory,
        CircuitBreakerFactory circuitBreakerFactory) {

        /* Serializer 설정 */
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                new GenericJackson2JsonRedisSerializer(objectMapper())));

        /* Cache TTL 설정 */
        Map<String, RedisCacheConfiguration> redisCacheConfigMap = new ConcurrentHashMap<>();
        redisCacheConfigMap.put(CacheNames.USER, defaultConfig.entryTtl(Duration.ofDays(1L)));
        redisCacheConfigMap.put(CacheNames.SCRAP, defaultConfig.entryTtl(Duration.ofDays(1L)));
        redisCacheConfigMap.put(CacheNames.DETAIL_MEMORY, defaultConfig.entryTtl(Duration.ofMinutes(30)));
        redisCacheConfigMap.put(CacheNames.GROUP, defaultConfig.entryTtl(Duration.ofMinutes(10)));

        RedisCacheManager redisCacheManager = RedisCacheManager.builder(connectionFactory)
            .withInitialCacheConfigurations(redisCacheConfigMap)
            .build();

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create(CACHE_CIRCUIT);
        return new CustomCacheManager(redisCacheManager, circuitBreaker);
    }
}
