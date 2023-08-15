package cmc.mellyserver.mellycore.config.mongodb;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@RequiredArgsConstructor
@EnableMongoRepositories(basePackages = "cmc.mellyserver.mellycore.notification.*")
@EnableAsync
public class MongodbConfig {

}