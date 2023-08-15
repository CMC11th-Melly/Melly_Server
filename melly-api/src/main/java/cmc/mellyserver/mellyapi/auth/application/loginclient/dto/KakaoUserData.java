package cmc.mellyserver.mellyapi.auth.application.loginclient.dto;

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

}