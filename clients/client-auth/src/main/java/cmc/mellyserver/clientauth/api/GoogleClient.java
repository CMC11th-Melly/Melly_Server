package cmc.mellyserver.clientauth.api;


import cmc.mellyserver.clientauth.LoginClient;
import cmc.mellyserver.clientauth.dto.GoogleUserData;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.enums.Provider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static cmc.mellyserver.dbcore.user.enums.Provider.GOOGLE;


@Component
@Slf4j
@RequiredArgsConstructor
public class GoogleClient implements LoginClient {

    private final GoogleLoginApi googleOpenFeign;

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
