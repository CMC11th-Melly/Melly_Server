package cmc.mellyserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class MellyApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MellyApiApplication.class, args);
    }

}
