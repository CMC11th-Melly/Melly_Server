package cmc.mellyserver.config;

import cmc.mellyserver.common.mockapi.MockAwsService;
import cmc.mellyserver.common.mockapi.MockFileUploader;
import cmc.mellyserver.common.util.aws.AwsService;
import cmc.mellyserver.common.util.aws.FileUploader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class InfrastructureTestConfiguration {

    @Bean
    public FileUploader fileUploader()
    {
        return new MockFileUploader();
    }

    @Bean
    public AwsService awsService()
    {
        return new MockAwsService();
    }
}
