package cmc.mellyserver.clientauth.api;


import cmc.mellyserver.clientauth.LoginClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class KakaoClient implements LoginClient {

    private final KakaoLoginApi kakaoLoginApi;

    @Override
    public boolean supports(String provider) {
        return provider == "kakao";
    }

    @Override
    public LoginClientResult getUserData(String accessToken) {

        return kakaoLoginApi.call(LoginClient.AUTH_PREFIX + accessToken).toResult();
    }
}
