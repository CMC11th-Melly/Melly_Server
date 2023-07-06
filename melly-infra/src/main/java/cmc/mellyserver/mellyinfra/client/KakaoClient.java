package cmc.mellyserver.mellyinfra.client;

import cmc.mellyserver.mellycommon.enums.Provider;
import cmc.mellyserver.mellycommon.enums.RoleType;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellyinfra.client.dto.KakaoUserData;
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
                .onStatus(HttpStatus::is4xxClientError,
                        clientResponse -> Mono.error(new IllegalArgumentException()))
                .onStatus(HttpStatus::is5xxServerError,
                        clientResponse -> Mono.error(
                                new IllegalArgumentException()))
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
