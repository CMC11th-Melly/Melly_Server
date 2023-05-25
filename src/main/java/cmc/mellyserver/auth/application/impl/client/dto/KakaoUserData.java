package cmc.mellyserver.auth.application.impl.client.dto;


import lombok.*;


import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class KakaoUserData {

    private Long id;

    private LocalDateTime connected_at;

}