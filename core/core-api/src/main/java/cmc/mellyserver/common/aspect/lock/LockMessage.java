package cmc.mellyserver.common.aspect.lock;

public abstract class LockMessage {

    public static final String OPTIMISTIC_LOCK_ACQUIRE_SUCCESS = "optimistic Lock try to acquire";

    public static final String OPTIMISTIC_LOCK_RETRY = "optimistic Lock acquire fail, retry start";

    public static final String OPTIMISTIC_LOCK_ACQUIRE_FAIL = "optimistic lock acquire eventually fail";

    public static final String DISTRIBUTED_LOCK_PREFIX = "LOCK:";
}
