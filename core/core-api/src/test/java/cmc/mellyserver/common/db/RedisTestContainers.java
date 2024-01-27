package cmc.mellyserver.common.db;

import org.junit.jupiter.api.DisplayName;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@DisplayName("Redis Test Containers")
@Configuration
@Profile("test")
public class RedisTestContainers {

    private static final String REDIS_DOCKER_IMAGE = "redis:5.0.3-alpine";
    private static final int TOKEN_CONTAINER_PORT = 6379;
    private static final int CACHE_CONTAINER_PORT = 6379;

    static {

        GenericContainer<?> TOKEN_CONTAINER = new GenericContainer<>(DockerImageName.parse(REDIS_DOCKER_IMAGE))
            .withExposedPorts(TOKEN_CONTAINER_PORT)
            .withReuse(true);

        GenericContainer<?> CACHE_CONTAINER = new GenericContainer<>(DockerImageName.parse(REDIS_DOCKER_IMAGE))
            .withExposedPorts(CACHE_CONTAINER_PORT)
            .withReuse(true);

        TOKEN_CONTAINER.start();
        CACHE_CONTAINER.start();

        System.setProperty("spring.redis.token.host", TOKEN_CONTAINER.getHost());
        System.setProperty("spring.redis.token.port", TOKEN_CONTAINER.getMappedPort(TOKEN_CONTAINER_PORT).toString());
        System.setProperty("spring.redis.cache.host", CACHE_CONTAINER.getHost());
        System.setProperty("spring.redis.cache.port", CACHE_CONTAINER.getMappedPort(CACHE_CONTAINER_PORT).toString());
    }
}