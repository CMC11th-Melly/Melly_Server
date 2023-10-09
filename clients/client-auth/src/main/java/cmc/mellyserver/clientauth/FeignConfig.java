package cmc.mellyserver.clientauth;

import feign.Logger;
import feign.Retryer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;


@EnableFeignClients("cmc.mellyserver.clientauth.api")
@Configuration
public class FeignConfig {

    /**
     * 0.1초부터 1초까지의 간격으로 3회 retry를 진행한다
     */
    @Bean
    Retryer.Default retryer() {
        return new Retryer.Default(100L, TimeUnit.SECONDS.toMillis(1L), 3);
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
