package cmc.mellyserver.auth.token;

import static cmc.mellyserver.auth.token.TokenConstants.*;

import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Component;

import cmc.mellyserver.dbredis.repository.RedisRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthTokenDao {

    private final RedisRepository repository;

    public void saveRefreshToken(RefreshToken refreshToken, Long refreshTokenExpiredTime) {
        repository.save(REFRESH_TOKEN_PREFIX + refreshToken.userId(), refreshToken.refreshToken(),
            refreshTokenExpiredTime);
    }

    public void makeAccessTokenDisabled(String accessToken, long lastExpiredTime) {
        repository.save(accessToken, ACCESS_TOKEN_BLACKLIST, lastExpiredTime);
    }

    public Optional<RefreshToken> findRefreshToken(Long userId) {

        String refreshToken = repository.get(REFRESH_TOKEN_PREFIX + userId);

        if (Objects.isNull(refreshToken)) {
            return Optional.empty();
        }

        return Optional.of(new RefreshToken(refreshToken, userId));
    }

    public void removeRefreshToken(Long userId) {
        repository.delete(REFRESH_TOKEN_PREFIX + userId);
    }
}
