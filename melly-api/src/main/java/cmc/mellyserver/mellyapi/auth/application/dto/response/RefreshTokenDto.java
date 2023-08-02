package cmc.mellyserver.mellyapi.auth.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefreshTokenDto {

    private String token;

    private Long expiredAt;

}
