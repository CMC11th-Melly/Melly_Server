package cmc.mellyserver.mellyapi.controller.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OAuthLoginResponseDto {

    private AccessTokenUserData user;
    private String token;
    private boolean isNewUser;

}
