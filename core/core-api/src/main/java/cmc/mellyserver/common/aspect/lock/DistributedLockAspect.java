package cmc.mellyserver.common.aspect.lock;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAspect {

    private final RedissonClient redissonClient;

    /*
    Lock을 획득하고 타겟 메서드를 실행한 뒤 UnLock 과정을 해줘야 하기에 @Around를 사용
     */
    @Around("@annotation(cmc.mellyserver.common.aspect.lock.DistributedLock)")
    public Object lock(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        Method method = signature.getMethod();

        DistributedLock lock = method.getAnnotation(DistributedLock.class);

		/*
		어노테이션에 있는 key 표현식을 기반으로 메서드 파라미터 값을 파싱한다
		@Cacheable 어노테이션에서 key값을 설정하는 방식과 유사하도록 구현
		 */
        String key = LockKeyParser.parse(signature.getParameterNames(), joinPoint.getArgs(), lock.key());
        RLock rLock = redissonClient.getLock(key);

        try {
            // 락 획득 시도
            boolean available = rLock.tryLock(lock.waitTime(), lock.leaseTime(), lock.timeUnit());

            // 락을 타임아웃 내에 획득하지 못했다면 예외 발생
            if (!available) {
                throw new BusinessException(ErrorCode.SERVER_ERROR);
            }

            /*
            락을 획득한 후 기존 메서드를 실행할때, 락을 사용하는 기능은 기존 트랜잭션에서 분리하기위해 REQUIRED_NEW를 사용했습니다.
            분산락이 적용된 기능을 완료한 뒤, 커밋하고 나면 그때 락을 해제하는 작업을 합니다.
            만약 기존의 트랜잭션을 그대로 이어받아 사용한다면 다른 클라이언트에 의해 갱신 손실이 발생할 수 있습니다.
             */
            return joinPoint.proceed();

        } catch (InterruptedException e) {
            throw new BusinessException(ErrorCode.SERVER_ERROR);
        } finally {
            rLock.unlock();
        }
    }
}
