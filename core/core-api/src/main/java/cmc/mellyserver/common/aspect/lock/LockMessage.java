package cmc.mellyserver.common.aspect.lock;

public abstract class LockMessage {

    // Optimistic Lock
    public static final String OPTIMISTIC_LOCK_AOP_ENTRY = "Optimistic Lock AOP 진입";

    public static final String OPTIMISTIC_LOCK_ACQUIRE_SUCCESS = "optimistic Lock try to acquire";

    public static final String OPTIMISTIC_LOCK_RETRY = "optimistic Lock acquire fail, retry start";

    public static final String OPTIMISTIC_LOCK_ACQUIRE_FAIL = "optimistic lock acquire eventually fail";

    public static final String OPTIMISTIC_LOCK_NOT_RETRY = "낙관적 락 버전 충돌 발생, 재시도 하지 않고 미반영";

    // Distributed Lock
    public static final String DISTRIBUTED_LOCK_AOP_ENTRY = "Distributed Lock AOP 진입";

    public static final String REDISSON_CONNECTION_FAIL = "Redisson Connection Fail";

    public static final String DISTRIBUTED_LOCK_PREFIX = "LOCK:";
}
