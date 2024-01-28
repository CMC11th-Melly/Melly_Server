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
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@EnableCaching
@Configuration
public class CacheConfig {

    private static final Duration COMMAND_TIMEOUT = Duration.ofMillis(200);

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

    /*
    캐시 TTL 시간 설정 기준
     - USER 프로필 캐시는 조회에 비해 변경 가능성이 적고 TTL로 1일을 설정했습니다.
     - SCRAP 캐시는 변경 가능성이 높지만 개인화된 데이터이고 TTL로 1일을 설정했습니다.
     - MEMORY 상세 페이지 캐시는 메모리 이외에도 그룹 정보, 장소 정보들이 혼합되기 때문에 TTL을 10분으로 짧게 설정했습니다.
     - GROUP 상세 페이지 캐시는 그룹 이외에도 유저 정보가 혼합되기 때문에 TTL을 10분으로 짧게 설정했습니다.
     */
    @Bean
    public CacheManager customCacheManager(CircuitBreakerFactory circuitBreakerFactory) {

        /* Serializer 설정 */
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                new GenericJackson2JsonRedisSerializer(objectMapper())));

        /* Cache TTL 설정 */
        Map<String, RedisCacheConfiguration> redisCacheConfigMap = new ConcurrentHashMap<>();
        redisCacheConfigMap.put(CacheNames.USER, defaultConfig.entryTtl(Duration.ofDays(1)));
        redisCacheConfigMap.put(CacheNames.SCRAP, defaultConfig.entryTtl(Duration.ofDays(1)));
        redisCacheConfigMap.put(CacheNames.MEMORY, defaultConfig.entryTtl(Duration.ofMinutes(10)));
        redisCacheConfigMap.put(CacheNames.GROUP, defaultConfig.entryTtl(Duration.ofMinutes(10)));

        RedisCacheManager redisCacheManager = RedisCacheManager.builder(redisCacheConnectionFactory())
            .withInitialCacheConfigurations(redisCacheConfigMap).build();

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create(CACHE_CIRCUIT);
        return new CustomCacheManager(redisCacheManager, circuitBreaker);
    }

    /*
    설정 종류
      - JavaTimeModule : Java 8의 date/time을 사용해서 string으로 직렬화
      - SerializationFeature.WRITE_DATES_AS_TIMESTAMP : 해당 속성을 true 시 Long 타입 직렬화, false는 String으로 직렬화
      - DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES : 역직렬화시 클래스 변수에 매핑되지 않는 값이 있을때 예외 발생 여부
    */
    @Bean
    public ObjectMapper objectMapper() {

        return new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}
