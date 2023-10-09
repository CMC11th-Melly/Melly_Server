package cmc.mellyserver.clientauth.api;

import cmc.mellyserver.clientauth.LoginClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class GoogleClient implements LoginClient {

    private final GoogleLoginApi googleLoginApi;

    @Override
    public boolean supports(String provider) {
        return provider == "google";
    }

    @Override
    public LoginClientResult getUserData(String accessToken) {

        return googleLoginApi.call(accessToken).toResult();
    }
}
