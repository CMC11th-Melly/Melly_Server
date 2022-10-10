package cmc.mellyserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class MellyServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MellyServerApplication.class, args);
    }

}
