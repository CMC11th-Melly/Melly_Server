package cmc.mellyserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//@EntityScan(basePackages = {"cmc.mellyserver.mellycore"})
//@EnableJpaRepositories(basePackages = {"cmc.mellyserver.mellycore"})
@EnableJpaAuditing
@SpringBootApplication
public class MellyApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MellyApiApplication.class, args);
	}

}
