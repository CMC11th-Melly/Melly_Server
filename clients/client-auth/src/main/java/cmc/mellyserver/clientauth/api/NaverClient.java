package cmc.mellyserver.clientauth.api;

import static cmc.mellyserver.clientauth.api.Provider.*;

import org.springframework.stereotype.Component;

import cmc.mellyserver.clientauth.LoginClient;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NaverClient implements LoginClient {

    private final NaverLoginApi naverLoginApi;

    @Override
    public boolean supports(String provider) {
        return provider.equals(NAVER);
    }

    @Override
    public LoginClientResult getUserData(String accessToken) {

        return naverLoginApi.call(accessToken).toResult();
    }

}
