package cmc.mellyserver.mellyapi.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableFeignClients(basePackages = "cmc.mellyserver.mellyapi.auth.application.loginclient.client.feign")
public class FeignConfig {
}
