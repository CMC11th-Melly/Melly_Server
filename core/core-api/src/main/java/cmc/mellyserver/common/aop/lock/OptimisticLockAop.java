package cmc.mellyserver.common.aop.lock;

import jakarta.persistence.OptimisticLockException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class OptimisticLockAop {
    private static int RETRY_MAX_COUNT = 3;

    @Around("@annotation(cmc.mellyserver.common.aop.lock.annotation.DistributedLock)")
    public Object doOneMoreRetryTransactionIfOptimisticLockExceptionThrow(ProceedingJoinPoint joinPoint) throws Throwable {

        Exception exceptionHolder = null;

        for (int retryCount = 0; retryCount <= RETRY_MAX_COUNT; retryCount++) {

            try {
                return joinPoint.proceed();

            } catch (OptimisticLockException | ObjectOptimisticLockingFailureException | CannotAcquireLockException e) {

                exceptionHolder = e;
            }
        }

        // 3번 반복 후에도 실패하면 예외 반환
        throw exceptionHolder;
    }
}
