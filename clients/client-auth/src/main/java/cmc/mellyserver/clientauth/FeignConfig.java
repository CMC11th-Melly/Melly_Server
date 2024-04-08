package cmc.mellyserver.clientauth;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients
@ImportAutoConfiguration({FeignAutoConfiguration.class})
@Configuration
public class FeignConfig {
}
