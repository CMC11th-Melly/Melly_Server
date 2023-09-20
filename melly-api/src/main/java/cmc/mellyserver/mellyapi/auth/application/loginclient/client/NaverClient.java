package cmc.mellyserver.mellyapi.auth.application.loginclient.client;


import cmc.mellyserver.mellyapi.auth.application.loginclient.LoginClient;
import cmc.mellyserver.mellyapi.auth.application.loginclient.client.feign.NaverOpenFeign;
import cmc.mellyserver.mellyapi.auth.application.loginclient.dto.NaverUserData;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.enums.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static cmc.mellyserver.mellycore.user.domain.enums.Provider.NAVER;

@Component
@RequiredArgsConstructor
public class NaverClient implements LoginClient {

    private final NaverOpenFeign naverOpenFeign;

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
