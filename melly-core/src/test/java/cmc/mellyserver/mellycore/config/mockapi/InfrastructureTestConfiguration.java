package cmc.mellyserver.mellycore.config.mockapi;


import cmc.mellyserver.mellycore.infrastructure.storage.StorageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class InfrastructureTestConfiguration {

    @Bean
    public StorageService mockFileUploader() {
        return new MockFileUploader();
    }

}
