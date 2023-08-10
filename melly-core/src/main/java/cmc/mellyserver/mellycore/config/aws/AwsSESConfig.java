package cmc.mellyserver.mellycore.config.aws;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(value = {"local", "prod"})
public class AwsSESConfig {
//    @Value("${aws.ses.access-key}")
//    private String accessKey;
//
//    @Value("${aws.ses.secret-key}")
//    private String secretKey;
//
//    @Bean
//    public AmazonSimpleEmailService amazonSimpleEmailService() {
//        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);
//        return AmazonSimpleEmailServiceClientBuilder.standard()
//                .withRegion(Regions.AP_NORTHEAST_2)
//                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
//                .build();
//    }
}