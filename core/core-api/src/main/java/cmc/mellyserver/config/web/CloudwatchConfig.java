package cmc.mellyserver.config.web;

import java.time.Duration;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.micrometer.cloudwatch2.CloudWatchConfig;
import io.micrometer.cloudwatch2.CloudWatchMeterRegistry;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;

@Configuration
@Profile("prod")
public class CloudwatchConfig {

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Bean
    public CloudWatchAsyncClient cloudWatchAsyncClient() {
        return CloudWatchAsyncClient.builder()
            .region(Region.AP_NORTHEAST_2)
            .credentialsProvider(
                StaticCredentialsProvider.create(new AwsCredentials() {
                    @Override
                    public String accessKeyId() {
                        return accessKey;
                    }

                    @Override
                    public String secretAccessKey() {
                        return secretKey;
                    }
                })
            )
            .build();
    }

    @Bean
    public MeterRegistry getMeterRegistry() {
        CloudWatchConfig cloudWatchConfig = setupCloudWatchConfig();

        MeterRegistry meterRegistry = new CloudWatchMeterRegistry(cloudWatchConfig, Clock.SYSTEM,
            cloudWatchAsyncClient());
        return meterRegistry;
    }

    private CloudWatchConfig setupCloudWatchConfig() {
        CloudWatchConfig cloudWatchConfig = new CloudWatchConfig() {

            private Map<String, String> configuration
                = Map.of("cloudwatch.namespace", "mellyApp",
                "cloudwatch.step", Duration.ofMinutes(1).toString());

            @Override
            public String get(String key) {
                return configuration.get(key);
            }
        };
        return cloudWatchConfig;
    }

}
