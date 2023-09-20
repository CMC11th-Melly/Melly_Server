package cmc.mellyserver.mellybatch.config;

import cmc.mellyserver.mellycore.common.policy.UserPolicy;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.enums.UserStatus;
import cmc.mellyserver.mellycore.user.domain.repository.UserRepository;
import cmc.mellyserver.mellycore.user.domain.enums.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class InactiveUserJobConfig {

    private final UserRepository userRepository;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job inactiveUserJob() {
        return jobBuilderFactory.get("inactiveUserJob")
                .preventRestart()
                .start(inactiveJobStep())
                .build();
    }

    @Bean
    public Step inactiveJobStep() {

        // (1)  StepBuilderFactory 주입
        return stepBuilderFactory.get("inactiveUserStep")
                // (2) chunk 사이즈 입력
                .<User, User>chunk(10)
                // (3) reader, processor, writer를 각각 설정
                .reader(inactiveUserReader())
                .processor(inactiveUserProcessor())
                .writer(inactiveUserWriter())
                .build();
    }

    @Bean
    @StepScope // (1) Step의 주기에 따라 새로운 빈 생성
    public QueueItemReader<User> inactiveUserReader() {
        List<User> oldUsers =
                userRepository.findByLastLoginDateTimeBeforeAndUserStatusEquals(
                        LocalDateTime.now().minusYears(UserPolicy.INACTIVE_USER_DURATION), UserStatus.ACTIVE);
        return new QueueItemReader<>(oldUsers);
    }

    public ItemProcessor<User, User> inactiveUserProcessor() {
        return new ItemProcessor<User, User>() {
            @Override
            public User process(User user) throws Exception {
                user.inActive();
                return user;
            }
        };
    }

    public ItemWriter<User> inactiveUserWriter() {
        return ((List<? extends User> users) -> userRepository.saveAll(users));
    }

}
