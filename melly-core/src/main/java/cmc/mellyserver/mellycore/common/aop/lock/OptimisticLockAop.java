package cmc.mellyserver.mellycore.common.aop.lock;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import javax.persistence.OptimisticLockException;

@Aspect
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE - 2)
@Component
public class OptimisticLockAop {


    public Integer retryMaxCount = 3;

    public Integer retryInterval = 3000;


    @Around("@annotation(cmc.mellyserver.mellycore.common.aop.lock.OptimisticLock)")
    public Object doOneMoreRetryTransactionIfOptimisticLockExceptionThrow(

            ProceedingJoinPoint joinPoint) throws Throwable {
        Exception exceptionHolder = null;

        for (int retryCount = 0; retryCount <= retryMaxCount; retryCount++) {

            try {
                log.info("[RETRY_COUNT]: {}", retryCount);
                log.info("Optimistic Locking aop 실행 후 다음 로직 넘어감");
                return joinPoint.proceed();

            } catch (OptimisticLockException | ObjectOptimisticLockingFailureException | CannotAcquireLockException e) {


                log.error("{} 발생", e.getClass());
                exceptionHolder = e;

                Thread.sleep(retryInterval);
            }
        }

        throw exceptionHolder;
    }
}
