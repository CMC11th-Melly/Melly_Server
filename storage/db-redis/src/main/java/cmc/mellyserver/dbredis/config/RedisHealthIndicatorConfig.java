package cmc.mellyserver.dbredis.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisHealthIndicatorConfig {

    /*
    Spring Actuator는 어플리케이션의 상태를 체크할때 연결된 모든 커넥션의 상태를 분석합니다.
    현재 어플리케이션에서는 Cache Server가 죽더라도 Circuit Breaker를 통해 Failover가 가능하도록 구현했습니다.
    하지만, 로드밸런싱 환경에서 로드밸런서는 연결된 서비스의 활성화 상태 체크를 위해서 Actuator의 /health endpoint를 체크합니다.
    이때 Redis Cache Server에 대한 커넥션이 끊겨있으면 Actuator는 해당 어플리케이션이 죽었다 판단하고 down을 반환합니다.
    따라서 로드밸런서 내의 모든 어플리케이션이 down을 반환하게 되고, 전체 서비스 장애로 이어질 수 있습니다.
    Redis의 Connection 상태가 어플리케이션 health check에 영향을 끼지치 않도록 무조건 UP을 반환하도록 변경했습니다.
     */
    @Bean
    public HealthIndicator redisHealthIndicator() {
        return () -> {
            try {
                return Health.up().build();
            } catch (Exception e) {
                return Health.down().build();
            }
        };
    }
}
