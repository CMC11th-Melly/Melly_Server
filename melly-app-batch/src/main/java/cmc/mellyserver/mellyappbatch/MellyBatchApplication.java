package cmc.mellyserver.mellyappbatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableBatchProcessing
@EnableScheduling
@SpringBootApplication
@EntityScan(basePackages = {"cmc.mellyserver.mellycore"})
@EnableJpaRepositories(basePackages = {"cmc.mellyserver.mellydomain"})
public class MellyBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(MellyBatchApplication.class, args);
    }

}
