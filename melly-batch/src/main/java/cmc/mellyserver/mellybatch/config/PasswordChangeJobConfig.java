package cmc.mellyserver.mellybatch.config;

import cmc.mellyserver.mellybatch.common.policy.AccountPolicy;
import cmc.mellyserver.mellycommon.enums.PasswordExpired;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.repository.UserRepository;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class PasswordChangeJobConfig {

    private final UserRepository userRepository;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job passwordChangeJob() {
        return jobBuilderFactory.get("passwordChangeJob").preventRestart()
                .start(passwordChangeStep()).build();
    }

    @Bean
    public Step passwordChangeStep() {

        // (1)  StepBuilderFactory 주입
        return stepBuilderFactory.get("passwordChangeStep")
                // (2) chunk 사이즈 입력
                .<User, User>chunk(10)
                // (3) reader, processor, writer를 각각 설정
                .reader(passwordChangeReader()).processor(passwordChangeProcessor())
                .writer(passwordChangeWriter()).build();
    }

    @Bean
    @StepScope
    public QueueItemReader<User> passwordChangeReader() {

        List<User> oldUsers = userRepository.findByPasswordInitDateBeforeAndPasswordExpiredEquals(
                LocalDateTime.now().minusMonths(AccountPolicy.PASSWORD_CHANGE_DURATION),
                PasswordExpired.N);
        return new QueueItemReader<>(oldUsers);
    }

    public ItemProcessor<User, User> passwordChangeProcessor() {
        return new ItemProcessor<User, User>() {
            @Override
            public User process(User user) throws Exception {
                return user.setPwChangeStatusAndUpdateLastChangedDate(LocalDate.now());
            }
        };
    }

    public ItemWriter<User> passwordChangeWriter() {
        return ((List<? extends User> users) -> userRepository.saveAll(users));
    }
}
