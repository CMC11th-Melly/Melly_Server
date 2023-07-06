package cmc.mellyserver.mellyinfra.client;


import cmc.mellyserver.mellycommon.enums.Provider;
import cmc.mellyserver.mellycommon.enums.RoleType;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellyinfra.client.dto.NaverUserData;
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
                        clientResponse -> Mono.error(new IllegalArgumentException()))
                .onStatus(HttpStatus::is5xxServerError,
                        clientResponse -> Mono.error(
                                new IllegalArgumentException()))
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
