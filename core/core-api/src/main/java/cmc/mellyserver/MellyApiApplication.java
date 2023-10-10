package cmc.mellyserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@EntityScan(basePackages = {"cmc.mellyserver.dbcore"})
@SpringBootApplication
public class MellyApiApplication {


    public static void main(String[] args) {
        SpringApplication.run(MellyApiApplication.class, args);
    }


}
