package cmc.mellyserver.clientauth.api;

import cmc.mellyserver.clientauth.LoginClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static cmc.mellyserver.clientauth.api.Provider.KAKAO;

@Component
@RequiredArgsConstructor
public class KakaoClient implements LoginClient {

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
