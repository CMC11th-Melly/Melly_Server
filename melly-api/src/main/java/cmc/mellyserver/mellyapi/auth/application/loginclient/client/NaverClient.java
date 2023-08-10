package cmc.mellyserver.mellyapi.auth.application.loginclient.client;


import cmc.mellyserver.mellyapi.auth.application.loginclient.LoginClient;
import cmc.mellyserver.mellyapi.auth.application.loginclient.dto.NaverUserData;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.enums.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static cmc.mellyserver.mellyapi.auth.application.loginclient.constants.OAuthConstants.NAVER_RESOURCE_SERVER_URL;
import static cmc.mellyserver.mellycore.user.domain.enums.Provider.NAVER;

@Component
@RequiredArgsConstructor
public class NaverClient implements LoginClient {

    private final WebClient webClient;

    @Override
    public boolean supports(Provider provider) {
        return provider == NAVER;
    }

    @Override
    public User getUserData(String accessToken) {

        NaverUserData naverUserResponse = webClient.get()
                .uri(NAVER_RESOURCE_SERVER_URL)
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new IllegalArgumentException()))
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new IllegalArgumentException()))
                .bodyToMono(NaverUserData.class)
                .block();

        return User.createOauthLoginUser(naverUserResponse.getResponse().getId(), NAVER, null, null, null, null);
    }
}
