package cmc.mellyserver.mellyappexternalapi.auth.application.impl.client;

import cmc.mellyserver.mellyappexternalapi.auth.application.LoginClient;
import cmc.mellyserver.mellyappexternalapi.auth.application.impl.client.dto.NaverUserData;
import cmc.mellyserver.mellyappexternalapi.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.mellyappexternalapi.common.exception.GlobalBadRequestException;
import cmc.mellyserver.mellyappexternalapi.common.exception.TokenValidFailedException;
import cmc.mellyserver.mellydomain.common.enums.Provider;
import cmc.mellyserver.mellydomain.common.enums.RoleType;
import cmc.mellyserver.mellydomain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class NaverClient implements LoginClient {

    private final WebClient webClient;

    @Override
    public boolean supports(Provider provider) {
        return provider == Provider.NAVER;
    }

    @Override
    public User getUserData(String accessToken) {

        NaverUserData naverUserResponse = webClient.get()
                .uri("https://openapi.naver.com/v1/nid/me")
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        clientResponse -> Mono.error(new GlobalBadRequestException(
                                ExceptionCodeAndDetails.NAVER_ACCESS)))
                .onStatus(HttpStatus::is5xxServerError,
                        clientResponse -> Mono.error(
                                new TokenValidFailedException("Internal Server error")))
                .bodyToMono(NaverUserData.class)
                .block();

        return User.builder()
                .uid(naverUserResponse.getResponse().getId())
                .provider(Provider.NAVER)
                .roleType(RoleType.USER)
                .password("NO_PASSWORD")
                .build();
    }
}
