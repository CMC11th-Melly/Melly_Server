package cmc.mellyserver.common.aspect.lock;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Order(1)
@Component
@RequiredArgsConstructor
public class OptimisticLockAspect {

    @Around("@annotation(cmc.mellyserver.common.aspect.lock.OptimisticLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        Method method = signature.getMethod();
        OptimisticLock lock = method.getAnnotation(OptimisticLock.class);

        long retryCount = lock.retryCount();
        long waitTime = lock.waitTime();

        for (int i = 0; i < retryCount; i++) {
            try {
                return joinPoint.proceed();
            } catch (OptimisticLockingFailureException e) {
                log.error(e.getMessage());
                Thread.sleep(waitTime);
            }
        }

        throw new BusinessException(ErrorCode.SERVER_ERROR);
    }

}
