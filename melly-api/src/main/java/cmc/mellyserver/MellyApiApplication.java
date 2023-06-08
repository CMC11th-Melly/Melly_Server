package cmc.mellyserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

//@EntityScan(basePackages = {"cmc.mellyserver.mellycore"})
//@EnableJpaRepositories(basePackages = {"cmc.mellyserver.mellycore"})

@EnableCaching
@SpringBootApplication
public class MellyApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MellyApiApplication.class, args);
    }

}
