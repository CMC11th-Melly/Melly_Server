package cmc.mellyserver.clientauth.model;

import java.time.LocalDateTime;

import cmc.mellyserver.clientauth.api.LoginClientResult;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class KakaoResource {

    private Long id;

    private Object kakao_account;

    private LocalDateTime connected_at;

    public LoginClientResult toResult() {
        return new LoginClientResult(String.valueOf(id), "kakao");
    }

}