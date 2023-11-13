package cmc.mellyserver.dbredis.repository;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FcmTokenRepository {

  private final StringRedisTemplate tokenRedisTemplate;

  public void saveToken(String key, String fcmToken) {
	tokenRedisTemplate.opsForValue().set(key, fcmToken);
  }

  public String getToken(String key) {
	return tokenRedisTemplate.opsForValue().get(key);
  }

  public void deleteToken(String key) {
	tokenRedisTemplate.delete(key);
  }

}
