package cmc.mellyserver.common.aspect.lock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class OptimisticLockAspect {

    // TODO : Retry Count와 Wait Time 산정 기준 명확히 하기
    private static final int RETRY_COUNT = 3;
    private static final int WAIT_TIME = 50;

    // TODO : Spring Retry가 아닌 직접 AOP를 구현한 이유를 명확히 설명하기
    // TODO : AOP에서 Object를 반환형으로 사용하면 어떻게 실제 로직은 구체 값을 받는지 제대로 체크하기
    @Around("@annotation(cmc.mellyserver.common.aspect.lock.OptimisticLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {

        for (int i = 0; i < RETRY_COUNT; i++) {

            try {
                return joinPoint.proceed();
            } catch (OptimisticLockException e) {
                Thread.sleep(WAIT_TIME);
            }
        }

        // TODO : Retry Count가 초과했을때, 시스템이 어떻게 동작해야 하는지를 확실히 하기
        throw new BusinessException(ErrorCode.SERVER_ERROR);
    }

}
