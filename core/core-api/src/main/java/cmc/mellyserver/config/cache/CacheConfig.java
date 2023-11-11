package cmc.mellyserver.config.cache;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import cmc.mellyserver.common.constants.CacheNames;

@EnableCaching
@Profile({"local", "prod"})
@Configuration
public class CacheConfig {

	@Value("${spring.redis.cache.host}")
	private String host;

	@Value("${spring.redis.cache.port}")
	private int port;

	@Bean(name = "redisCacheConnectionFactory")
	RedisConnectionFactory redisCacheConnectionFactory() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setHostName(host);
		redisStandaloneConfiguration.setPort(port);
		return new LettuceConnectionFactory(redisStandaloneConfiguration);
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
	 */
	@Bean
	public RedisCacheManager redisCacheManager(
		@Qualifier("redisCacheConnectionFactory") RedisConnectionFactory connectionFactory) {

		RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
			.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
				new GenericJackson2JsonRedisSerializer(objectMapper())));

		Map<String, RedisCacheConfiguration> redisCacheConfigMap = new HashMap<>();
		redisCacheConfigMap.put(CacheNames.USER, defaultConfig.entryTtl(Duration.ofHours(1)));
		redisCacheConfigMap.put(CacheNames.MEMORY, defaultConfig.entryTtl(Duration.ofHours(1)));
		redisCacheConfigMap.put(CacheNames.FEED, defaultConfig.entryTtl(Duration.ofMinutes(1)));
		redisCacheConfigMap.put(CacheNames.GROUP, defaultConfig.entryTtl(Duration.ofHours(1)));

		return RedisCacheManager.builder(connectionFactory)
			.withInitialCacheConfigurations(redisCacheConfigMap)
			.build();
	}

}
