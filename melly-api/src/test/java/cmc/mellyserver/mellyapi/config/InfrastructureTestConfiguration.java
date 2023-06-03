package cmc.mellyserver.mellyapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import cmc.mellyserver.mellyapi.common.aws.AwsService;
import cmc.mellyserver.mellyapi.common.aws.FileUploader;
import cmc.mellyserver.mellyapi.common.mockapi.MockAwsService;
import cmc.mellyserver.mellyapi.common.mockapi.MockFileUploader;

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
