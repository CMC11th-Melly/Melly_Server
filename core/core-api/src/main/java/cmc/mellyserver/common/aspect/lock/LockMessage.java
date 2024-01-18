package cmc.mellyserver.common.aspect.lock;

public abstract class LockMessage {

    // Optimistic Lock
    public static final String OPTIMISTIC_LOCK_AOP_ENTRY = "Optimistic Lock AOP 진입";

    public static final String OPTIMISTIC_LOCK_RETRY = "optimistic Lock acquire fail, retry start";

    public static final String OPTIMISTIC_LOCK_ACQUIRE_FAIL = "optimistic lock acquire eventually fail";
}
