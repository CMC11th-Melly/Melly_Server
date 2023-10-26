package cmc.mellyserver.config.cache;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@EnableCaching
@Configuration
@Profile({ "local", "prod" })
public class CacheConfig {

	@Autowired
	RedisConnectionFactory redisConnectionFactory;

	/**
	 * 설정 종류
	 * <p>
	 * SerializationFeature.INDENT_OUTPUT : 콘솔에 출력할때 포맷팅해서 나옵니다.
	 * <p>
	 * JavaTimeModule : 해당 모듈을 등록해줘야 Java 8의 date/time을 사용해서 string으로 직렬화 가능합니다.
	 * <p>
	 * SerializationFeature.WRITE_DATES_AS_TIMESTAMP : 해당 속성을 true로 설정하면 Long 타입으로 직렬화됩니다.
	 * 현재 Disable로 설정해서 String으로 직렬화되도록 설정했습니다.
	 * <p>
	 * DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES : 역직렬화시 클래스 변수에 매핑되지 않는 값이 있을때
	 * 예외를 발생시킬지 체크, 현재는 예외가 발생하지 않도록 false로 설정했습니다.
	 */
	@Bean
	public ObjectMapper objectMapper() {

		return new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)
			.registerModule(new JavaTimeModule())
			.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	/**
	 * 설정 종류
	 * <p>
	 * - 서비스 특성 상 수정 작업보다 읽기 작업이 많이 발생하고, 현재 데이터 규모가 크지 않기에 초단위의 캐시 Eviction은 불필요하다고
	 * 판단했습니다.
	 * <p>
	 * - 데이터 수정 시 즉시 CacheEvict나 CachePut을 진행해서 데이터의 최신성을 보장하도록 구현했기에 TTL로 인한 데이터 최신성 문제는
	 * 현재 고려하지 않아도 됩니다.
	 * <p>
	 * - cache key는 String으로 직렬화 진행합니다.
	 * <p>
	 * - cache value는 여러 타입이 들어올 수 있기에 GenericJackon2JsonRedisSerializer를 사용했습니다.
	 */
	@Bean
	public RedisCacheManager redisCacheManager() {

		RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
			.entryTtl(Duration.ofSeconds(60))
			.disableCachingNullValues()
			.serializeValuesWith(RedisSerializationContext.SerializationPair
				.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper())));

		return RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(redisCacheConfiguration).build();
	}

}
