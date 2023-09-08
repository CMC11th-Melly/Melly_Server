package cmc.mellyserver.mellycore.common.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
@Order(Ordered.LOWEST_PRECEDENCE - 1)
@Slf4j
public class DistributedLockAop {

    private static final String REDISSON_LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;

    // 해당 어노테이션이 붙은 메서드를 찾아온다.
    @Around("@annotation(cmc.mellyserver.mellycore.common.aop.DistributedLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {

        // @DistributedLock 어노테이션이 붙은 메서드의 시그니처를 가져온다.
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 해당 메서드의 어노테이션 정보를 가져온다.
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        // DistributedLock 어노테이션에 넘긴 key 파라미터 값을 파싱해서 LOCK:12 형태로 만든다.
        String key = REDISSON_LOCK_PREFIX + CustomSpringELParser.getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), distributedLock.key());

        // 락을 조회한다.
        RLock rLock = redissonClient.getLock(key);
        redissonClient.getConfig().useReplicatedServers().setRetryAttempts(3).setRetryInterval(1000);
        try {
            // 락이 사용 가능한지 체크한뒤, 옵션들을 설정한다.
            boolean available = rLock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit());  // (2)

            // 만약 누가 락을 점유하고 있으면 false 반환
            if (!available) {
                return false;
            }

            // 본 로직 실행
            return aopForTransaction.proceed(joinPoint);

        } catch (InterruptedException e) {
            throw new InterruptedException();

        } finally {
            try {
                // 본 로직 실행이 성공적으로 끝나고 나면 락을 해제한다. 이때 대기하고 있던 쓰레드들에게 락 해제 메세지가 전달 된다.
                rLock.unlock();
            } catch (IllegalMonitorStateException e) {
                // 실패하면 예외 발생
                log.info("Redisson Lock Already UnLock {} {}", method.getName(), key);
            }
        }
    }
}
