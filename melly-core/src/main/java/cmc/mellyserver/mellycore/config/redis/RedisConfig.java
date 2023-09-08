package cmc.mellyserver.mellycore.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {

        return new LettuceConnectionFactory(host, port);
    }


    @Bean
    public RedisTemplate<?, ?> redisTemplate() {

        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }

//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
//        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(config);
//        jedisConnectionFactory.setPoolConfig(jedisPoolConfig());
//        return jedisConnectionFactory;
//    }
//
//    private JedisPoolConfig jedisPoolConfig() {
//        final JedisPoolConfig poolConfig = new JedisPoolConfig();
//        poolConfig.setMaxTotal(128);
//        poolConfig.setMaxIdle(128);
//        poolConfig.setMinIdle(36);
//        poolConfig.setTestOnBorrow(true);
//        poolConfig.setTestOnReturn(true);
//        poolConfig.setTestWhileIdle(true);
//        poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
//        poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
//        poolConfig.setNumTestsPerEvictionRun(3);
//        poolConfig.setBlockWhenExhausted(true);
//        return poolConfig;
//    }


}

//@Slf4j
//@RequiredArgsConstructor
//@RestController
//public class ApiController {
//    private final AvailablePointRedisRepository availablePointRedisRepository;
//
//    @GetMapping("/")
//    public String ok() {
//        return "ok";
//    }
//
//    @GetMapping("/save")
//    public String save() {
//        String randomId = createId();
//        LocalDateTime now = LocalDateTime.now();
//        AvailablePoint availablePoint = AvailablePoint.builder()
//                .id(randomId)
//                .point(1L)
//                .refreshTime(now)
//                .build();
//        log.info(">>>>>>> [save] availablePoint={}", availablePoint);
//        availablePointRedisRepository.save(availablePoint);
//        return "save";
//    }
//
//    @GetMapping("/get")
//    public long get() {
//        String id = createId();
//        return availablePointRedisRepository.findById(id)
//                .map(AvailablePoint::getPoint)
//                .orElse(0L);
//    }
//
//    // 임의의 키를 생성하기 위해 1 ~ 1_000_000_000 사이 랜덤값 생성
//    private String createId() {
//        SplittableRandom random = new SplittableRandom();
//        return String.valueOf(random.nextInt(1, 1_000_000_000));
//    }
//}