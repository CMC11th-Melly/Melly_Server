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

    @Around("@annotation(cmc.mellyserver.mellycore.common.aop.lock.annotation.OptimisticLock)")
    public Object doOneMoreRetryTransactionIfOptimisticLockExceptionThrow(


            ProceedingJoinPoint joinPoint) throws Throwable {

        Exception exceptionHolder = null;

        for (int retryCount = 0; retryCount <= retryMaxCount; retryCount++) {

            try {
                return joinPoint.proceed();

            } catch (OptimisticLockException | ObjectOptimisticLockingFailureException | CannotAcquireLockException e) {

                exceptionHolder = e;

            }
        }

        throw exceptionHolder;
    }
}
