package cmc.mellyserver.common.aspect.lock;

import static cmc.mellyserver.common.aspect.lock.LockMessage.*;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import cmc.mellyserver.support.exception.CommonException;
import cmc.mellyserver.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
@Order(Ordered.LOWEST_PRECEDENCE - 1)
@RequiredArgsConstructor
public class OptimisticLockAspect {

    @Around("@annotation(cmc.mellyserver.common.aspect.lock.OptimisticLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {

        log.info(OPTIMISTIC_LOCK_AOP_ENTRY);

        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        Method method = signature.getMethod();
        OptimisticLock lock = method.getAnnotation(OptimisticLock.class);

        int retryCount = lock.retryCount();
        long waitTime = lock.waitTime();

        for (int i = 0; i < retryCount; i++) {
            try {
                return joinPoint.proceed();
            } catch (OptimisticLockingFailureException e) {
                log.info(OPTIMISTIC_LOCK_RETRY);
                Thread.sleep(waitTime);
            }
        }

        log.error(OPTIMISTIC_LOCK_ACQUIRE_FAIL);
        throw new CommonException(ErrorCode.SERVER_ERROR);
    }
}
