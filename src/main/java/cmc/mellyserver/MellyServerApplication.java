package cmc.mellyserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class MellyServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MellyServerApplication.class, args);
    }

}
