package cmc.mellyserver.clientauth.api;

import static cmc.mellyserver.clientauth.api.Provider.*;

import org.springframework.stereotype.Component;

import cmc.mellyserver.clientauth.LoginClient;
import lombok.RequiredArgsConstructor;

/*
타 모듈은 Client 호출이 필요할때 xxClient 객체를 사용해서 호출하면 됩니다.
OpenFeign 인터페이스에 대한 접근은 패키지 내부로 숨겼습니다.
 */
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
