package cmc.mellyserver.common.aop.lock.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    // 락 이름
    String key();

    // 락 시간 단위
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    // 락 대기 시간
    long waitTime() default 3L;

    // 락 사용 시간
    // 락을 3초 사용한 이후에는 반납해야 한다.
    long leaseTime() default 2L;

}