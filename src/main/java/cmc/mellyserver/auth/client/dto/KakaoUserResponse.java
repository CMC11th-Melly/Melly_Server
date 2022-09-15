package cmc.mellyserver.auth.client.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class KakaoUserResponse {

    private Long id;

    private LocalDateTime connected_at;
//    @Nullable
//    private Properties properties;
//    @Nullable
//    private KakaoAccount kakaoAccount;
//
//    @ToString
//    @Getter
//    @NoArgsConstructor
//    @AllArgsConstructor
//   @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
//    public static class Properties {
//        @Nullable
//        private String nickname;
//    }
//
//    @ToString
//    @Getter
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class KakaoAccount {
//        @Nullable
//        private String email;
//    }
}