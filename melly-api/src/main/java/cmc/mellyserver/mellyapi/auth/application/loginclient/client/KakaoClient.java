package cmc.mellyserver.mellyapi.auth.application.loginclient.client;

import cmc.mellyserver.mellyapi.auth.application.loginclient.LoginClient;
import cmc.mellyserver.mellyapi.auth.application.loginclient.client.feign.KakaoOpenFeign;
import cmc.mellyserver.mellyapi.auth.application.loginclient.dto.KakaoUserData;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.enums.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static cmc.mellyserver.mellycore.user.domain.enums.Provider.KAKAO;

@Component
@RequiredArgsConstructor
public class KakaoClient implements LoginClient {

    private final KakaoOpenFeign kakaoOpenFeign;

    @Override
    public boolean supports(Provider provider) {
        return provider == KAKAO;
    }

    @Override
    public User getUserData(String accessToken) {

        KakaoUserData kakaoUserResponse = kakaoOpenFeign.call(AUTH_PREFIX + accessToken);
        return User.createOauthLoginUser(String.valueOf(kakaoUserResponse.getId()), KAKAO, null, null, null, null);
    }
}
