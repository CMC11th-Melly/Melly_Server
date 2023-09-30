package cmc.mellyserver.controller.auth.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class AuthResponseForLogin {

    private AccessTokenUserData user;
    private String token;

    @Builder
    public AuthResponseForLogin(AccessTokenUserData user, String token) {
        this.user = user;
        this.token = token;
    }
}
