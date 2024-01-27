package cmc.mellyserver.auth.token;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FcmTokenRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public void saveToken(String key, String fcmToken) {
        redisTemplate.opsForValue().set(key, fcmToken);
    }

    public String getToken(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteToken(String key) {
        redisTemplate.delete(key);
    }

}
