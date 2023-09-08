package cmc.mellyserver.mellycore.config.redis;

import java.time.Duration;

public abstract class CacheExpireConstants {

    public static final Duration DEFAULT_CACHE_EXPIRE_TIME = Duration.ofMinutes(60 * 24);
}
