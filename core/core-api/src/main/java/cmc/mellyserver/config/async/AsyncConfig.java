package cmc.mellyserver.config.async;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * AsyncConfigurer를 구현하면 getAsyncExecutor() 메서드에서 반환된 Executor를 Default로 사용한다.
 */
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

	/**
	 * 스프링은 기본적으로 SimpleAsyncTaskExecutor를 사용합니다. 이 방식은 요청이 올때마다 쓰레드를 새로 생성합니다.
	 * <p>
	 * 이 방식은 사용자 요청이 몰리는 상황에서는 쓰레드 생성으로 인한 리소스 낭비가 발생합니다. 따라서 미리 쓰레드를 생성해두는
	 * ThreadPoolTaskExecutor를 사용합니다. ThreadPoolTaskExecutor는 java.util.concurrent
	 * package의 ThreadPoolExecutor를 더 편하게 사용하기 위한 Spring의 클래스입니다.
	 * <p>
	 * Core Pool Size - 처음 생성되는 쓰레드 개수
	 * <p>
	 * Queue Capacity - Core Pool Size가 처리할 수 있는 Task 이상이 들어오면 Queue에 쌓이기 시작합니다. 큐의 크기를
	 * 나타내는 수치
	 * <p>
	 * Max Pool Size - Queue가 꽉 찼을때, 최대로 늘어날 수 있는 쓰레드의 개수입니다. Queue Capacity가 가득 찬 시점부터 이미
	 * 문제가 있는 것이기 때문에 Max Pool Size를 늘리는건 적절한 대책이 아니라 생각했습니다.
	 * <p>
	 * setWaitForTasksToCompleteOnShutdown - Spring Container가 shutdown 될때 기존에 대기중이던
	 * task들을 모두 처리할때까지 block 하도록 설정합니다.
	 * <p>
	 * setAwaitTerminationSeconds - 무한정 종료를 대기할 수 없기때문에 최대 기다리는 시간을 설정합니다.
	 */
	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(10);
		executor.setQueueCapacity(10000);
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.setAwaitTerminationSeconds(10);
		executor.initialize();
		return executor;
	}

	/**
	 * 비동기 메서드 호출 시 발생하는 예외는 MVC의 @ControllerAdvice에서 처리하지 못합니다.
	 * <p>
	 * 따라서 별도의 AsyncExceptionHandler를 통해 비동기 예외를 처리해야 합니다.
	 */
	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new AsyncExceptionHandler();
	}

}
