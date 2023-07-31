package cmc.mellyserver.mellycore.unit.config.mockapi;


import cmc.mellyserver.mellycore.common.aws.AwsService;
import cmc.mellyserver.mellycore.common.aws.StorageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
public class InfrastructureTestConfiguration {

    @Bean
    public StorageService fileUploader() {
        return new MockFileUploader();
    }

    @Bean
    public AwsService awsService() {
        return new MockAwsService();
    }
}
