package cmc.mellyserver.auth.client;

import cmc.mellyserver.auth.client.dto.KakaoUserResponse;
import cmc.mellyserver.auth.client.dto.NaverUserResponse;
import cmc.mellyserver.auth.exception.TokenValidFailedException;
import cmc.mellyserver.auth.presentation.dto.Provider;
import cmc.mellyserver.user.domain.RoleType;
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
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new TokenValidFailedException("Social Access Token is unauthorized")))
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new TokenValidFailedException("Initail Server error")))
                .bodyToMono(NaverUserResponse.class)
                .block();

        return User.builder()
                .userId(naverUserResponse.getResponse().getId())
                .provider(Provider.NAVER)
                .nickname(naverUserResponse.getResponse().getNickname())
                .roleType(RoleType.USER)
                .email(naverUserResponse.getResponse().getEmail())
                .build();
    }
}
