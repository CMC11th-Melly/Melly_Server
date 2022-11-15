package cmc.mellyserver.auth.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccessTokenResponse {

    private int code;
    private String message;
    private AccessTokenUserData user;
    private String token;
}
