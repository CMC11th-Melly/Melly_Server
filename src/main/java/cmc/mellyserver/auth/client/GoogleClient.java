package cmc.mellyserver.auth.client;

import cmc.mellyserver.auth.client.dto.GoogleUserResponse;
import cmc.mellyserver.auth.exception.TokenValidFailedException;
import cmc.mellyserver.auth.presentation.dto.Provider;
import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.user.domain.RoleType;
import cmc.mellyserver.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class GoogleClient implements Client{

    private final WebClient webClient;

    @Override
    public User getUserData(String accessToken) {
        GoogleUserResponse googleUserResponse = webClient.get()



                .uri("https://oauth2.googleapis.com/tokeninfo", builder -> builder.queryParam("id_token", accessToken).build())

                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new GlobalBadRequestException(ExceptionCodeAndDetails.GOOGLE_ACCESS)))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new TokenValidFailedException("Internal Server Error")))
                .bodyToMono(GoogleUserResponse.class)
                .block();
        System.out.println("제발 " +googleUserResponse.getSub());

        return User.builder()
                .userId(googleUserResponse.getSub())
                .email(googleUserResponse.getEmail())
                .provider(Provider.GOOGLE)
                .roleType(RoleType.USER).build();



    }
}
