package cmc.mellyserver.clientauth.dto;

import cmc.mellyserver.clientauth.api.LoginClientResult;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class KakaoUserData {

    private Long id;

    private Object kakao_account;

    private LocalDateTime connected_at;

    public LoginClientResult toResult() {
        return new LoginClientResult(String.valueOf(id), "kakao");
    }
}