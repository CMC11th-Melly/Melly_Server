package cmc.mellyserver.auth.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OAuthLoginResponse {

    private int code;
    private String message;
    private AccessTokenUserData user;
    private String token;
    private boolean isNewUser;

}
