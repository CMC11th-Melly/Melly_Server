package cmc.mellyserver.mellycore.infrastructure.message.fcm;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class FCMTokenRepository {

    private final StringRedisTemplate tokenRedisTemplate;

    public void saveToken(String email, String fcmToken) {
        tokenRedisTemplate.opsForValue()
                .set(email, fcmToken);
    }

    public String getToken(String email) {
        return tokenRedisTemplate.opsForValue().get(email);
    }

    public void deleteToken(String email) {
        tokenRedisTemplate.delete(email);
    }

    public boolean hasKey(String email) {
        return tokenRedisTemplate.hasKey(email);
    }
}
