package cmc.mellyserver.mellyapi.auth.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static cmc.mellyserver.mellyapi.common.constants.RedisConstants.REFRESH_TOKEN_PREFIX;

@Repository
public class RefreshTokenRepository {

    private RedisTemplate redisTemplate;

    public RefreshTokenRepository(final RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(final RefreshToken refreshToken, final Long refreshTokenExpire) {
        redisTemplate.opsForValue().set(REFRESH_TOKEN_PREFIX + refreshToken.getUserId(), refreshToken.getRefreshToken(), refreshTokenExpire, TimeUnit.MILLISECONDS);

    }

    public Optional<RefreshToken> findById(final Long userId) {
        ValueOperations<Long, String> valueOperations = redisTemplate.opsForValue();
        String refreshToken = valueOperations.get(REFRESH_TOKEN_PREFIX + userId);

        if (Objects.isNull(refreshToken)) {
            return Optional.empty();
        }

        return Optional.of(new RefreshToken(refreshToken, userId));
    }

    public void remove(final Long userId) {

        redisTemplate.delete(REFRESH_TOKEN_PREFIX + userId);
    }
}
