package cmc.mellyserver.common.aop.lock;

import cmc.mellyserver.common.aop.lock.annotation.DistributedLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributedLockAop {

	private static final String REDISSON_LOCK_PREFIX = "LOCK:";

	private final RedissonClient redissonClient;

	@Around("@annotation(cmc.mellyserver.common.aop.lock.annotation.DistributedLock)")
	public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {

		// @DistributedLock 어노테이션이 붙은 메서드의 시그니처를 가져온다.
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		// 해당 메서드의 어노테이션 정보를 가져온다.
		DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

		// DistributedLock 어노테이션에 넘긴 key 파라미터 값을 파싱해서 LOCK:12 형태로 만든다.
		/*
		 * signature.getParameternames() => 메서드 시그니쳐에 들어가는 파라미터 이름 joinPoint.getArgs() =>
		 * 실제 groupService#participatedToGroup()에 파라미터로 넘어온 값 parser의 의미 => 프록시로 넘어온 파라미터
		 * 중에서 @DistributedLock의 key 이름에 해당하는 변수의 값을 파싱함
		 */
		String key = REDISSON_LOCK_PREFIX + CustomSpringELParser.getDynamicValue(signature.getParameterNames(),
				joinPoint.getArgs(), distributedLock.key());

		RLock rLock = redissonClient.getLock(key);

		try {

			/*
			 * tryLock 메서드에 진입한 쓰레드는 락을 획득할 수 있을때까지 대기하게 됩니다. Pub/Sub 기반으로 동작하며, 락을 가지고 있는
			 * 쓰레드가 락을 해제하면 해당 소식을 publish하게 되고, subscribe 하고 있던 쓰레드 중 하나가 락을 획득합니다. 이 방식의
			 * 장점은 스핀락 방식을 사용하지 않기 때문에 Redis로 가는 부하가 적다는 장점이 있습니다.
			 */
			boolean available = rLock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(),
					distributedLock.timeUnit());

			if (!available) {
				throw new IllegalArgumentException("락 획득 실패로 로직 실행에 실패했습니다");
			}

			return joinPoint.proceed();

		}
		catch (InterruptedException e) {
			throw new InterruptedException();
		}
		finally {
			try {
				rLock.unlock();
			}
			catch (IllegalMonitorStateException e) {
				log.error("Redisson Lock이 이미 해제됐습니다. {} {}", method.getName(), key);
			}
		}
	}

}
