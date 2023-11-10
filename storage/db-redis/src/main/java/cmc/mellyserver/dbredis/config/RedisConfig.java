package cmc.mellyserver.dbredis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

	@Value("${spring.data.redis.host}")
	private String host;

	@Value("${spring.data.redis.port}")
	private int port;

	/*
	Redis를 Cluster mode가 아닌 single node 모드로 사용하기에 RedisStandaloneConfiguration을 사용했습니다.
	 */
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {

		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setHostName(host);
		redisStandaloneConfiguration.setPort(port);
		return new LettuceConnectionFactory(redisStandaloneConfiguration);
	}

	/*
	RedisTemplate의 key는 String 타입을 사용합니다.
	Value는 어떤 타입이든 들어올 수 있기 때문에 GenericJackson2JsonRedisSerializer를 사용합니다.
	 */
	@Bean
	public RedisTemplate<?, ?> redisTemplate() {

		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
		return redisTemplate;
	}
}