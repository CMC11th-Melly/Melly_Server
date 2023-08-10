package cmc.mellyserver.mellyapi.auth.application.loginclient.client;


import cmc.mellyserver.mellyapi.auth.application.loginclient.LoginClient;
import cmc.mellyserver.mellyapi.auth.application.loginclient.constants.OAuthConstants;
import cmc.mellyserver.mellyapi.auth.application.loginclient.dto.GoogleUserData;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.enums.Provider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static cmc.mellyserver.mellycore.user.domain.enums.Provider.GOOGLE;

@Component
@Slf4j
@RequiredArgsConstructor
public class GoogleClient implements LoginClient {

    private final WebClient webClient;

    @Override
    public boolean supports(Provider provider) {
        return provider == GOOGLE;
    }

    @Override
    public User getUserData(String accessToken) {

        GoogleUserData googleUserResponse = webClient.get()
                .uri(OAuthConstants.GOOGLE_RESOURCE_SERVER_URL, builder -> builder.queryParam("id_token", accessToken).build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new IllegalArgumentException()))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new IllegalAccessException()))
                .bodyToMono(GoogleUserData.class)
                .block();

        return User.createOauthLoginUser(googleUserResponse.getSub(), GOOGLE, null, null, null, null);
    }
}
