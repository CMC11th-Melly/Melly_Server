package cmc.mellyserver.config.async;

import static cmc.mellyserver.config.async.ExecutorConstants.*;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class AsyncConfig implements AsyncConfigurer {

    private static final String IMAGE_EXECUTOR_PREFIX = "image-upload-thread-pool-";
    private static final String NOTIFICATION_EXECUTOR_PREFIX = "notification-thread-pool-";
    private static final String SEND_EMAIL_EXECUTOR_PREFIX = "send-email-thread-pool-";
    private static final String ASYNC_DEFAULT_EXECUTOR_PREFIX = "async-default-thread-pool-";

    @Bean(IMAGE_EXECUTOR_BEAN_NAME)
    public ThreadPoolTaskExecutor imageUploadTaskExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(1000);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(5);
        executor.setThreadNamePrefix(IMAGE_EXECUTOR_PREFIX);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    @Bean(NOTIFICATION_BEAN_NAME)
    public ThreadPoolTaskExecutor notificationTaskExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(1000);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(5);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        executor.setThreadNamePrefix(NOTIFICATION_EXECUTOR_PREFIX);
        executor.initialize();
        return executor;
    }

    @Bean(SEND_EMAIL_BEAN_NAME)
    public ThreadPoolTaskExecutor sendEmailTaskExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(1000);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(5);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        executor.setThreadNamePrefix(SEND_EMAIL_EXECUTOR_PREFIX);
        executor.initialize();
        return executor;
    }

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(1000);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(5);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix(ASYNC_DEFAULT_EXECUTOR_PREFIX);
        executor.initialize();
        return executor;
    }
}
