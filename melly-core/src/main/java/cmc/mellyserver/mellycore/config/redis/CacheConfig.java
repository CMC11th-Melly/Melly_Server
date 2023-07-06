package cmc.mellyserver.mellycore.config.redis;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

//    @Autowired
//    RedisConnectionFactory redisConnectionFactory;


    public ObjectMapper objectMapper() {

        PolymorphicTypeValidator typeValidator = BasicPolymorphicTypeValidator
                .builder().allowIfSubType(Object.class)
                .build();

        return new ObjectMapper()
                .findAndRegisterModules()
                .activateDefaultTyping(typeValidator, ObjectMapper.DefaultTyping.NON_FINAL,
                        JsonTypeInfo.As.PROPERTY)
                .registerModules(new JavaTimeModule());


    }

//
//    @Bean
//    public CacheManager redisCacheManager() {
//
//        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(
//                        new StringRedisSerializer()))
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
//                        new GenericJackson2JsonRedisSerializer(objectMapper())))
//                .entryTtl(Duration.ofSeconds(20));
//
//        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory).cacheDefaults(redisCacheConfiguration).build();
//    }

//    @Bean
//    public CacheManager cacheManager() {
//        return new ConcurrentMapCacheManager();
//    }

}
