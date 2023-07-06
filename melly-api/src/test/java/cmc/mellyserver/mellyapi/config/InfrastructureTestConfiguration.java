package cmc.mellyserver.mellyapi.config;

import cmc.mellyserver.mellyapi.common.mockapi.MockAwsService;
import cmc.mellyserver.mellyapi.common.mockapi.MockFileUploader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class InfrastructureTestConfiguration {

    @Bean
    public FileUploader fileUploader() {
        return new MockFileUploader();
    }

    @Bean
    public AwsService awsService() {
        return new MockAwsService();
    }
}
