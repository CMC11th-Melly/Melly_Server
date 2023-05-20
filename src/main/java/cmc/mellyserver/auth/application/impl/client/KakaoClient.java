package cmc.mellyserver.auth.application.impl.client;

import cmc.mellyserver.auth.application.LoginClient;
import cmc.mellyserver.auth.application.impl.client.dto.KakaoUserData;
import cmc.mellyserver.common.exception.TokenValidFailedException;
import cmc.mellyserver.common.enums.Provider;
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
public class KakaoClient implements LoginClient {

    private final WebClient webClient;

    @Override
    public boolean supports(Provider provider) {
        return provider == Provider.KAKAO;
    }

    @Override
    public User getUserData(String accessToken) {

        KakaoUserData kakaoUserResponse = webClient.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new GlobalBadRequestException(ExceptionCodeAndDetails.KAKAO_ACCESS)))
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new TokenValidFailedException("Internal Server error")))
                .bodyToMono(KakaoUserData.class)
                .block();

        return User.builder()
                .uid(String.valueOf(kakaoUserResponse.getId()))
                .provider(Provider.KAKAO)
                .roleType(RoleType.USER)
                .password("NO_PASSWORD")
                .build();
    }
}
