package cmc.mellyserver.mellyapi.auth.application.loginclient.client;

import cmc.mellyserver.mellyapi.auth.application.loginclient.LoginClient;
import cmc.mellyserver.mellyapi.auth.application.loginclient.dto.KakaoUserData;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.enums.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static cmc.mellyserver.mellyapi.auth.application.loginclient.constants.OAuthConstants.KAKAO_RESOURCE_SERVER_URL;
import static cmc.mellyserver.mellycore.user.domain.enums.Provider.KAKAO;

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
