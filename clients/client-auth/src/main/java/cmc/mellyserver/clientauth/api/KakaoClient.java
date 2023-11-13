package cmc.mellyserver.clientauth.api;

import static cmc.mellyserver.clientauth.api.Provider.*;

import org.springframework.stereotype.Component;

import cmc.mellyserver.clientauth.LoginClient;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KakaoClient implements LoginClient {

  private static final String AUTH_PREFIX = "Bearer ";

  private final KakaoLoginApi kakaoLoginApi;

  @Override
  public boolean supports(String provider) {
	return provider.equals(KAKAO);
  }

  @Override
  public LoginClientResult getUserData(String accessToken) {

	return kakaoLoginApi.call(AUTH_PREFIX + accessToken).toResult();
  }
}
