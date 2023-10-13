package cmc.mellyserver.mellybatch.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class PasswordChangeJobConfig {

//    private final UserRepository userRepository;
//    private final JobBuilderFactory jobBuilderFactory;
//    private final StepBuilderFactory stepBuilderFactory;
//
//    @Bean
//    public Job passwordChangeJob() {
//        return jobBuilderFactory.get("passwordChangeJob").preventRestart()
//                .start(passwordChangeStep()).build();
//    }
//
//    @Bean
//    public Step passwordChangeStep() {
//
//        // (1)  StepBuilderFactory 주입
//        return stepBuilderFactory.get("passwordChangeStep")
//                // (2) chunk 사이즈 입력
//                .<User, User>chunk(10)
//                // (3) reader, processor, writer를 각각 설정
//                .reader(passwordChangeReader()).processor(passwordChangeProcessor())
//                .writer(passwordChangeWriter()).build();
//    }
//
//    @Bean
//    @StepScope
//    public QueueItemReader<User> passwordChangeReader() {
//
//        List<User> oldUsers = userRepository.findByPwInitDateTimeBeforeAndPasswordExpiredEquals(
//                LocalDateTime.now().minusMonths(6),
//                PasswordExpired.N);
//        return new QueueItemReader<>(oldUsers);
//    }
//
//    public ItemProcessor<User, User> passwordChangeProcessor() {
//        return new ItemProcessor<User, User>() {
//            @Override
//            public User process(User user) throws Exception {
//
//                return null;
//
//            }
//        };
//    }
//
//    public ItemWriter<User> passwordChangeWriter() {
//        return ((List<? extends User> users) -> userRepository.saveAll(users));
//    }
}
