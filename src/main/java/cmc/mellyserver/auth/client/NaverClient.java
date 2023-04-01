package cmc.mellyserver.auth.client;

import cmc.mellyserver.auth.client.dto.NaverUserResponse;
import cmc.mellyserver.auth.exception.TokenValidFailedException;
import cmc.mellyserver.auth.presentation.dto.Provider;
import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.enums.RoleType;
import cmc.mellyserver.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class NaverClient implements Client{

    private final WebClient webClient;

    @Override
    public User getUserData(String accessToken) {

        NaverUserResponse naverUserResponse = webClient.get()
                .uri("https://openapi.naver.com/v1/nid/me")
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new GlobalBadRequestException(ExceptionCodeAndDetails.NAVER_ACCESS)))
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new TokenValidFailedException("Internal Server error")))
                .bodyToMono(NaverUserResponse.class)
                .block();

        return User.builder()
                .uid(naverUserResponse.getResponse().getId())
                .provider(Provider.NAVER)
                .roleType(RoleType.USER)
                .password("NO_PASSWORD")
                .build();
    }
}
