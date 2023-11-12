package cmc.mellyserver.config.cache;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import cmc.mellyserver.common.constants.CacheNames;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.EntryRemovedEvent;
import io.github.resilience4j.core.registry.EntryReplacedEvent;
import io.github.resilience4j.core.registry.RegistryEventConsumer;
import lombok.extern.slf4j.Slf4j;

@EnableCaching
@Configuration
@Slf4j
public class CacheConfig {

	@Value("${spring.redis.cache.host}")
	private String host;

	@Value("${spring.redis.cache.port}")
	private int port;

	private static final String CACHE_CURCUIT_BREAKER = "cache_curcuit_breaker";

	@Bean(name = "redisCacheConnectionFactory")
	RedisConnectionFactory redisCacheConnectionFactory() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setHostName(host);
		redisStandaloneConfiguration.setPort(port);
		LettuceClientConfiguration lettuceClientConfiguration = LettuceClientConfiguration.builder()
			.commandTimeout(Duration.ofSeconds(1))
			.build();

		return new LettuceConnectionFactory(redisStandaloneConfiguration, lettuceClientConfiguration);
	}

	/*
	설정 종류
     - SerializationFeature.INDENT_OUTPUT : 콘솔에 출력할때 포맷팅해서 나옵니다.
	 - JavaTimeModule : 해당 모듈을 등록해줘야 Java 8의 date/time을 사용해서 string으로 직렬화 가능합니다.
	 - SerializationFeature.WRITE_DATES_AS_TIMESTAMP : 해당 속성을 true로 설정하면 Long 타입으로 직렬화됩니다. 현재 Disable로 설정해서 String으로 직렬화되도록 설정했습니다.
	 - DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES : 역직렬화시 클래스 변수에 매핑되지 않는 값이 있을때 예외를 발생시킬지 체크, 현재는 예외가 발생하지 않도록 false로 설정했습니다.
	 */
	@Bean
	public ObjectMapper objectMapper() {

		return new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)
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
	 - Memory 리스트인 FEED는 수정이 잦을 것으로 예상되어 1분으로 TTL을 설정했습니다.
	 - Group 데이터도 수정이 적을 것으로 예상되어 1시간으로 TTL을 설정했습니다.

	Circuit breaker를 통한 HA 보장
	레디스로 분산 캐시를 구현 시 레디스 서버가 다운될 수 있습니다. 이때 Spring Cache는 자동으로 DB 조회를 하는 것이 아니라 예외가 발생합니다.
	이를 해결하기 위해 레디스 서버 장애시 fallback으로 DB 조회를 수행하고, 임계치 이상 예외 발생 시, Circuit을 Open하고 Redis 서버 조회 자체를
	막음으로써 Fail Fast를 통한 Redis Server Recovery를 유도했습니다.
	 */
	@Bean
	public CacheManager redisCacheManager(
		@Qualifier("redisCacheConnectionFactory") RedisConnectionFactory connectionFactory) {

		RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
			.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
				new GenericJackson2JsonRedisSerializer(objectMapper())));

		Map<String, RedisCacheConfiguration> redisCacheConfigMap = new HashMap<>();
		redisCacheConfigMap.put(CacheNames.USER, defaultConfig.entryTtl(Duration.ofHours(1)));
		redisCacheConfigMap.put(CacheNames.MEMORY, defaultConfig.entryTtl(Duration.ofHours(1)));
		redisCacheConfigMap.put(CacheNames.FEED, defaultConfig.entryTtl(Duration.ofMinutes(1)));
		redisCacheConfigMap.put(CacheNames.GROUP, defaultConfig.entryTtl(Duration.ofHours(1)));

		RedisCacheManager redisCacheManager = RedisCacheManager.builder(connectionFactory)
			.withInitialCacheConfigurations(redisCacheConfigMap)
			.build();

		CircuitBreakerRegistry circuitBreakerRegistry = configCircuitBreaker();

		return new CustomCacheManager(redisCacheManager, circuitBreakerRegistry.circuitBreaker(CACHE_CURCUIT_BREAKER));
	}

	private CircuitBreakerRegistry configCircuitBreaker() {

		CircuitBreakerConfig config = CircuitBreakerConfig
			.custom()
			.slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
			.slidingWindowSize(5)
			.failureRateThreshold(80)
			.waitDurationInOpenState(Duration.ofSeconds(20))
			.permittedNumberOfCallsInHalfOpenState(4)
			.automaticTransitionFromOpenToHalfOpenEnabled(true)
			.recordExceptions(CallNotPermittedException.class, QueryTimeoutException.class)
			.build();

		CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.custom().withCircuitBreakerConfig(config)
			.addRegistryEventConsumer(new RegistryEventConsumer<CircuitBreaker>() {

				@Override
				public void onEntryAddedEvent(EntryAddedEvent<CircuitBreaker> entryAddedEvent) {

					CircuitBreaker.EventPublisher eventPublisher = entryAddedEvent.getAddedEntry().getEventPublisher();

					eventPublisher.onStateTransition(event -> log.info("onStateTransition {}", event));
					eventPublisher.onError(event -> log.error("onError {}", event));
					eventPublisher.onSuccess(event -> log.info("onSuccess {}", event));
					eventPublisher.onCallNotPermitted(event -> log.info("onCallNotPermitted {}", event));
				}

				@Override
				public void onEntryRemovedEvent(EntryRemovedEvent<CircuitBreaker> entryRemoveEvent) {

				}

				@Override
				public void onEntryReplacedEvent(EntryReplacedEvent<CircuitBreaker> entryReplacedEvent) {

				}
			}).build();
		return circuitBreakerRegistry;
	}

}
