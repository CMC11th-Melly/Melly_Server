package cmc.mellyserver.clientauth.api;


import cmc.mellyserver.clientauth.LoginClient;
import cmc.mellyserver.clientauth.dto.KakaoUserData;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.enums.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static cmc.mellyserver.dbcore.user.enums.Provider.KAKAO;


@Component
@RequiredArgsConstructor
public class KakaoClient implements LoginClient {

    private final KakaoLoginApi kakaoOpenFeign;

    @Override
    public boolean supports(Provider provider) {
        return provider == KAKAO;
    }

    @Override
    public User getUserData(String accessToken) {

        KakaoUserData kakaoUserResponse = kakaoOpenFeign.call(LoginClient.AUTH_PREFIX + accessToken);
        return User.createOauthLoginUser(String.valueOf(kakaoUserResponse.getId()), KAKAO, null, null, null, null);
    }
}
