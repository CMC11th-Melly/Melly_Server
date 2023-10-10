package cmc.mellyserver.clientauth.api;

import cmc.mellyserver.clientauth.LoginClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static cmc.mellyserver.clientauth.api.Provider.GOOGLE;


@Component
@RequiredArgsConstructor
public class GoogleClient implements LoginClient {

    private final GoogleLoginApi googleLoginApi;

    @Override
    public boolean supports(String provider) {
        return provider.equals(GOOGLE);
    }

    @Override
    public LoginClientResult getUserData(String accessToken) {

        return googleLoginApi.call(accessToken).toResult();
    }
}
