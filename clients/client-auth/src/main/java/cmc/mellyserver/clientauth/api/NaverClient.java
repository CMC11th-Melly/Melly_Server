package cmc.mellyserver.clientauth.api;


import cmc.mellyserver.clientauth.LoginClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static cmc.mellyserver.clientauth.api.Provider.NAVER;


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
