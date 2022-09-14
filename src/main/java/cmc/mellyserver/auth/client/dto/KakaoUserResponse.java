package cmc.mellyserver.auth.client.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.springframework.lang.Nullable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class KakaoUserResponse {

    private Long id;
    @Nullable
    private Properties properties;
    @Nullable
    private KakaoAccount kakaoAccount;

    @ToString
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
   @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class Properties {
        @Nullable
        private String nickname;
    }

    @ToString
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KakaoAccount {
        @Nullable
        private String email;
    }
}