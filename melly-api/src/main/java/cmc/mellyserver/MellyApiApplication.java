package cmc.mellyserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EntityScan(basePackages = {"cmc.mellyserver.mellycore"})
//@EnableJpaRepositories(basePackages = {"cmc.mellyserver.mellycore"})

@SpringBootApplication
public class MellyApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MellyApiApplication.class, args);
    }

}
