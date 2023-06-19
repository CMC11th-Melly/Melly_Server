package cmc.mellyserver.mellyappexternalapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import cmc.mellyserver.mellyappexternalapi.common.aws.AwsService;
import cmc.mellyserver.mellyappexternalapi.common.aws.FileUploader;
import cmc.mellyserver.mellyappexternalapi.common.mockapi.MockAwsService;
import cmc.mellyserver.mellyappexternalapi.common.mockapi.MockFileUploader;

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
