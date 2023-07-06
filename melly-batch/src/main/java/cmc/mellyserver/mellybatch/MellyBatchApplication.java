package cmc.mellyserver.mellybatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableBatchProcessing
@EnableScheduling
@SpringBootApplication
//@EntityScan(basePackages = {"cmc.mellyserver.mellycore"})
//@EnableJpaRepositories(basePackages = {"cmc.mellyserver.mellycore"})
public class MellyBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(MellyBatchApplication.class, args);
    }

}
