package cmc.mellyserver.auth.client.dto;


import lombok.*;


import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class KakaoUserResponse {

    private Long id;

    private LocalDateTime connected_at;

}