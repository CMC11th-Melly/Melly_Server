package cmc.mellyserver.mellyapi.auth.application.loginclient.client;


import cmc.mellyserver.mellyapi.auth.application.loginclient.LoginClient;
import cmc.mellyserver.mellyapi.auth.application.loginclient.client.feign.GoogleOpenFeign;
import cmc.mellyserver.mellyapi.auth.application.loginclient.dto.GoogleUserData;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.enums.Provider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static cmc.mellyserver.mellycore.user.domain.enums.Provider.GOOGLE;

@Component
@Slf4j
@RequiredArgsConstructor
public class GoogleClient implements LoginClient {

    private final GoogleOpenFeign googleOpenFeign;

    @Override
    public boolean supports(Provider provider) {
        return provider == GOOGLE;
    }

    @Override
    public User getUserData(String accessToken) {

        GoogleUserData googleUserResponse = googleOpenFeign.call(accessToken);
        return User.createOauthLoginUser(googleUserResponse.getSub(), GOOGLE, null, null, null, null);
    }
}
