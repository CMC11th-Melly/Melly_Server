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
public class DistributedLockAop {

    private final RedissonClient redissonClient;

    @Around("@annotation(cmc.mellyserver.common.aspect.lock.DistributedLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {

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

            // 락을 획득했다면 실제 메소드 실행
            return joinPoint.proceed();

        } catch (InterruptedException e) {
            throw new BusinessException(ErrorCode.SERVER_ERROR);
        } finally {
            rLock.unlock();
        }
    }

}
