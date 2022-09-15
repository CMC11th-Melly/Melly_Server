package cmc.mellyserver.auth.client;

import cmc.mellyserver.auth.client.dto.KakaoUserResponse;
import cmc.mellyserver.auth.exception.TokenValidFailedException;
import cmc.mellyserver.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Component
@RequiredArgsConstructor
public class KakaoClient implements Client{

    private final WebClient webClient;

    @Override
    public User getUserData(String accessToken) {
        KakaoUserResponse kakaoUserResponse = webClient.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new TokenValidFailedException("Social Access Token is unauthorized")))
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new TokenValidFailedException("Initail Server error")))
                .bodyToMono(KakaoUserResponse.class)
                .block();

//        return Member.builder()
//                .socialId(String.valueOf(kakaoUserResponse.getId()))
//                .nickname(kakaoUserResponse.getProperties().getNickname())
//                .email(kakaoUserResponse.getKakaoAccount().getEmail())
//                .build();
        System.out.println("어떤 값 들어있나? " + kakaoUserResponse.toString());
        return User.builder()
                .userId(kakaoUserResponse.getId())
                .build();
    }
}
