package cmc.mellyserver.notification.fcm;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class FCMTokenRepository {

    private final StringRedisTemplate tokenRedisTemplate;

    public void saveToken(String key, String fcmToken) {
        tokenRedisTemplate.opsForValue()
                .set(key, fcmToken);
    }

    public String getToken(String key) {
        return tokenRedisTemplate.opsForValue().get(key);
    }

    public void deleteToken(String key) {
        tokenRedisTemplate.delete(key);
    }

    public boolean hasKey(String key) {
        return tokenRedisTemplate.hasKey(key);
    }
}
