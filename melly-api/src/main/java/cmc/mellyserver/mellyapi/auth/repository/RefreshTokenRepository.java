package cmc.mellyserver.mellyapi.auth.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class RefreshTokenRepository {

    private RedisTemplate redisTemplate;

    public RefreshTokenRepository(final RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(final RefreshToken refreshToken, final Long refreshTokenExpire) {
        redisTemplate.opsForValue().set(refreshToken.getRefreshToken(), refreshToken.getUserId());
        redisTemplate.expire(refreshToken.getRefreshToken(), refreshTokenExpire, TimeUnit.MILLISECONDS);
    }

    public Optional<RefreshToken> findById(final String refreshToken) {
        ValueOperations<String, Long> valueOperations = redisTemplate.opsForValue();
        Long userId = valueOperations.get(refreshToken);

        if (Objects.isNull(userId)) {
            return Optional.empty();
        }

        return Optional.of(new RefreshToken(refreshToken, userId));
    }
}
