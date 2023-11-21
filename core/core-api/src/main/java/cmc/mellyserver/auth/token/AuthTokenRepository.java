package cmc.mellyserver.auth.token;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AuthTokenRepository {

    private final RedisTemplate redisTemplate;

    public void saveRefreshToken(RefreshToken refreshToken, Long refreshTokenExpiredTime) {
        redisTemplate.opsForValue()
            .set(TokenConstants.REFRESH_TOKEN_PREFIX + refreshToken.userId(), refreshToken.refreshToken(),
                refreshTokenExpiredTime, TimeUnit.MILLISECONDS);
    }

    public void makeAccessTokenDisabled(String accessToken, long lastExpiredTime) {
        redisTemplate.opsForValue()
            .set(accessToken, TokenConstants.ACCESS_TOKEN_BLACKLIST, lastExpiredTime, TimeUnit.MILLISECONDS);
    }

    public Optional<RefreshToken> findRefreshToken(Long userId) {
        ValueOperations<Long, String> valueOperations = redisTemplate.opsForValue();
        String refreshToken = valueOperations.get(TokenConstants.REFRESH_TOKEN_PREFIX + userId);

        if (Objects.isNull(refreshToken)) {
            return Optional.empty();
        }

        return Optional.of(new RefreshToken(refreshToken, userId));
    }

    public void removeRefreshToken(Long userId) {

        redisTemplate.delete(TokenConstants.REFRESH_TOKEN_PREFIX + userId);
    }

}
