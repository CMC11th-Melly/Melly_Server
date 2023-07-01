package cmc.mellyserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.PropertySource;

//@EntityScan(basePackages = {"cmc.mellyserver.mellycore"})
//@EnableJpaRepositories(basePackages = {"cmc.mellyserver.mellycore"})

@PropertySource({"classpath:application-core.yml"})
@EnableCaching
@SpringBootApplication
public class MellyApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MellyApiApplication.class, args);
    }

}
