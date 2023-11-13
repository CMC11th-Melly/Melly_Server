package cmc.mellyserver.auth.repository;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import cmc.mellyserver.auth.token.TokenProvider;
import cmc.mellyserver.common.constants.RedisConstants;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AuthTokenRepository {

  private final RedisTemplate redisTemplate;

  private final TokenProvider tokenProvider;

  public void saveRefreshToken(final RefreshToken refreshToken, final Long refreshTokenExpire) {
	redisTemplate.opsForValue()
		.set(RedisConstants.REFRESH_TOKEN_PREFIX + refreshToken.getUserId(), refreshToken.getRefreshToken(),
			refreshTokenExpire, TimeUnit.MILLISECONDS);
  }

  public void makeAccessTokenDisabled(final String accessToken) {
	redisTemplate.opsForValue()
		.set(accessToken, RedisConstants.ACCESS_TOKEN_BLACKLIST, tokenProvider.getLastExpireTime(accessToken),
			TimeUnit.MILLISECONDS);
  }

  public Optional<RefreshToken> findRefreshToken(final Long userId) {
	ValueOperations<Long, String> valueOperations = redisTemplate.opsForValue();
	String refreshToken = valueOperations.get(RedisConstants.REFRESH_TOKEN_PREFIX + userId);

	if (Objects.isNull(refreshToken)) {
	  return Optional.empty();
	}

	return Optional.of(new RefreshToken(refreshToken, userId));
  }

  public void removeRefreshToken(final Long userId) {

	redisTemplate.delete(RedisConstants.REFRESH_TOKEN_PREFIX + userId);
  }

}
