package cmc.mellyserver.mellyinfra.client.client;

import cmc.mellyserver.mellycommon.enums.Provider;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellyinfra.client.LoginClient;
import cmc.mellyserver.mellyinfra.client.dto.KakaoUserData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static cmc.mellyserver.mellycommon.enums.Provider.KAKAO;
import static cmc.mellyserver.mellyinfra.common.constant.OAuthConstants.KAKAO_RESOURCE_SERVER_URL;

@Component
@RequiredArgsConstructor
public class KakaoClient implements LoginClient {

    private final WebClient webClient;

    @Override
    public boolean supports(Provider provider) {
        return provider == KAKAO;
    }

    @Override
    public User getUserData(String accessToken) {

        KakaoUserData kakaoUserResponse = webClient.get()
                .uri(KAKAO_RESOURCE_SERVER_URL)
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new IllegalArgumentException()))
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new IllegalArgumentException()))
                .bodyToMono(KakaoUserData.class)
                .block();
        System.out.println(kakaoUserResponse.getId());
        return User.createOauthLoginUser(String.valueOf(kakaoUserResponse.getId()), KAKAO, null, null, null, null);
    }
}
