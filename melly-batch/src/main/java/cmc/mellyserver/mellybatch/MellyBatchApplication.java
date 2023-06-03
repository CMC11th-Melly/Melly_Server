package cmc.mellyserver.mellybatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class MellyBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(MellyBatchApplication.class, args);
	}

}
