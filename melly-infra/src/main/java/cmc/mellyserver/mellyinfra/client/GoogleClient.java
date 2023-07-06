package cmc.mellyserver.mellyinfra.client;


import cmc.mellyserver.mellycommon.enums.Provider;
import cmc.mellyserver.mellycommon.enums.RoleType;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellyinfra.client.dto.GoogleUserData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class GoogleClient implements LoginClient {

    private final WebClient webClient;

    @Override
    public boolean supports(Provider provider) {
        return provider == Provider.GOOGLE;
    }

    @Override
    public User getUserData(String accessToken) {

        GoogleUserData googleUserResponse = webClient.get()
                .uri("https://oauth2.googleapis.com/tokeninfo",
                        builder -> builder.queryParam("id_token", accessToken).build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        response -> Mono.error(new IllegalArgumentException()))
                .onStatus(HttpStatus::is5xxServerError,
                        response -> Mono.error(new IllegalAccessException()))
                .bodyToMono(GoogleUserData.class)
                .block();

        return User.builder()
                .uid(googleUserResponse.getSub())
                .provider(Provider.GOOGLE)
                .password("NO_PASSWORD")
                .roleType(RoleType.USER).build();

    }
}
