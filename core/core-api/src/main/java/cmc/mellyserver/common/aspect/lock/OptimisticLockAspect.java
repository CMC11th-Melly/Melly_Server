package cmc.mellyserver.common.aspect.lock;

import static cmc.mellyserver.common.aspect.lock.LockMessage.*;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
트랜잭션이 커밋될때 낙관적 락을 위한 버전 체크가 발생합니다. 이때 버전 충돌이 발생하면 예외가 반환됩니다.
해당 AOP 어드바이저는 OptimisticLockingFailureException을 캐치해서 트랜잭션을 재시도 해야 합니다.
따라서 트랜잭션보다 외부에 AOP를 생성하는 방법을 적용했습니다.

AOP는 Order의 숫자가 작을수록 우선순위가 높습니다. @Transactional은 default가 LOWEST_PRECEDENCE으로 가장 우선순위가 낮습니다.
따라서 LOWEST_PRECEDENCE보다 한 단계 우선하도록 LOWEST_PRECEDENCE-1을 적용했습니다.
 */
@Slf4j
@Aspect
@Component
@Order(Ordered.LOWEST_PRECEDENCE - 2) // @Transaction보다 먼저 AOP가 호출
@RequiredArgsConstructor
public class OptimisticLockAspect {

    @Around("@annotation(cmc.mellyserver.common.aspect.lock.OptimisticLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {

        log.info(OPTIMISTIC_LOCK_AOP_ENTRY); // 낙관적 락 진입 확인

        // @OptimisticLock 어노테이션 값 획득
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        Method method = signature.getMethod();
        OptimisticLock lock = method.getAnnotation(OptimisticLock.class);

        int retryCount = lock.retryCount();
        long waitTime = lock.waitTime();

        for (int i = 0; i < retryCount; i++) {

            try {
                return joinPoint.proceed();

            } catch (OptimisticLockingFailureException | CannotAcquireLockException ex) {
                log.info(OPTIMISTIC_LOCK_RETRY);
                Thread.sleep(waitTime);
            }
        }

        log.error(OPTIMISTIC_LOCK_ACQUIRE_FAIL);
        throw new BusinessException(ErrorCode.SERVER_ERROR);
    }
}
