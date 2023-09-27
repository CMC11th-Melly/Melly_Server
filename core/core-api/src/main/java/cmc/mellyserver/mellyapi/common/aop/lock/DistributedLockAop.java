package cmc.mellyserver.mellyapi.common.aop.lock;

import cmc.mellyserver.mellyapi.common.aop.lock.annotation.DistributedLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
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

    // TODO : 락을 구현하는 방법은 여러가지가 사용 가능하다. 이 부분 추상화 필요!
    private final RedissonClient redissonClient;


    /*
    반환값이 Object인 이유 : @DistributedLock 어노테이션이 어떤 메서드에 사용될지 미리 예상할 수 없다.
                         void가 될수도 있고, String이 반환될 수도 있는 것이기에 Object형을 통해 응답을 받도록 구성함
     */
//    @Around("@annotation(d1)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {

        // @DistributedLock 어노테이션이 붙은 메서드의 시그니처를 가져온다.
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 해당 메서드의 어노테이션 정보를 가져온다.
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        // DistributedLock 어노테이션에 넘긴 key 파라미터 값을 파싱해서 LOCK:12 형태로 만든다.
        /*
        signature.getParameternames() => 메서드 시그니쳐에 들어가는 파라미터 이름
        joinPoint.getArgs() => 실제 groupService#participatedToGroup()에 파라미터로 넘어온 값
        parser의 의미 => 프록시로 넘어온 파라미터 중에서 @DistributedLock의 key 이름에 해당하는 변수의 값을 파싱함
         */
        String key = REDISSON_LOCK_PREFIX + CustomSpringELParser.getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), distributedLock.key());

        RLock rLock = redissonClient.getLock(key);

        try {

            /*
            1. 락 사용이 가능하다면 available을 true로 반환
            2. 락 사용이 불가능하다면 기존 사용자가 unlock 할때까지 대기
             */
            boolean available = rLock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit());

            if (!available) { // 끝까지 락 획득 못했으면 예외 발생
                throw new IllegalArgumentException("락 획득 실패로 로직 실행에 실패했습니다");
            }

            return joinPoint.proceed();

        } catch (InterruptedException e) {
            throw new InterruptedException();

        } finally {
            try {
                rLock.unlock();
            } catch (IllegalMonitorStateException e) {
                log.error("Redisson Lock이 이미 해제됐습니다. {} {}", method.getName(), key);
            }
        }
    }
}
