package cmc.mellyserver.config.cache;

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
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableCaching
@Configuration
public class CacheConfig {

    @Value("${spring.redis.cache.host}")
    private String host;
    @Value("${spring.redis.cache.port}")
    private int port;

    /*
    Redis를 운영 환경에서 사용할때는 Sentinel이나 Cluster 모드를 사용해서 고가용성을 보장할 것이고,
    RedisConfiguration으로 RedisSentinelConfiguration이나 RedisClusterConfiguration을 사용할 것입니다.
    현재 프로젝트에서는 가용성 보장을 위한 Replication을 하지는 못했기에 싱글 노드 기반의 RedisStandaloneConfiguration을 사용했습니다.
     */
    @Bean(name = "redisCacheConnectionFactory")
    RedisConnectionFactory redisCacheConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);

        /*
        Redis 서버가 다운됐을 시, Redis에 요청을 보낸 뒤 200ms동안 응답이 없으면 Timeout이 발생하도록 구현했습니다.
        현재 프로젝트 내 Redis 캐시의 응답 시간이 보통 100ms 아래로 나오는 것을 참고했을때 200ms로 command timeout을 설정한다면,
        Redis 서버가 죽지 않은 상황에서 일시적인 지연으로 인해 Timeout이 발생하고, DB 쿼리가 발생하는 상황을 방지할 수 있다고 생각했습니다.
         */
        LettuceClientConfiguration lettuceClientConfiguration = LettuceClientConfiguration.builder()
            .commandTimeout(Duration.ofMillis(200L))
            .build();

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

    /*
    설정 종류
     - cache key는 String으로 직렬화 진행합니다.
     - cache value는 여러 타입이 들어올 수 있기에 GenericJackon2JsonRedisSerializer를 사용했습니다.
     - User 데이터는 수정이 적을 것으로 예상되어 1시간으로 TTL을 설정했습니다.
     - Memory 데이터도 수정이 적을 것으로 예상되어 1시간으로 TTL을 설정했습니다.
     - Group 데이터도 수정이 적을 것으로 예상되어 1시간으로 TTL을 설정했습니다.

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
        redisCacheConfigMap.put(CacheNames.USER, defaultConfig.entryTtl(Duration.ofHours(1)));
        redisCacheConfigMap.put(CacheNames.MEMORY, defaultConfig.entryTtl(Duration.ofHours(1)));
        redisCacheConfigMap.put(CacheNames.GROUP, defaultConfig.entryTtl(Duration.ofHours(1)));
        redisCacheConfigMap.put(CacheNames.SCRAP, defaultConfig.entryTtl(Duration.ofHours(1)));

        RedisCacheManager redisCacheManager = RedisCacheManager.builder(connectionFactory)
            .withInitialCacheConfigurations(redisCacheConfigMap)
            .build();

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create(CACHE_CIRCUIT);

        /* Circuit Breaker 설정 */
        return new CustomCacheManager(redisCacheManager, circuitBreaker);
    }

}
