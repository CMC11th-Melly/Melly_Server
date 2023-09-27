package cmc.mellyserver.clientauth.api;


import cmc.mellyserver.clientauth.LoginClient;
import cmc.mellyserver.clientauth.dto.NaverUserData;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.enums.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static cmc.mellyserver.dbcore.user.enums.Provider.NAVER;


@Component
@RequiredArgsConstructor
public class NaverClient implements LoginClient {

    private final NaverLoginApi naverOpenFeign;

    @Override
    public boolean supports(Provider provider) {
        return provider == NAVER;
    }

    @Override
    public User getUserData(String accessToken) {

        NaverUserData naverUserResponse = naverOpenFeign.call(accessToken);
        return User.createOauthLoginUser(naverUserResponse.getResponse().getId(), NAVER, null, null, null, null);
    }
}
