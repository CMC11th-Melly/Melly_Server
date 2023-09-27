package cmc.mellyserver.mellyapi.controller.auth.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponse {

    private AccessTokenUserData user;
    private String token;

    @Builder
    public LoginResponse(AccessTokenUserData user, String token) {
        this.user = user;
        this.token = token;
    }
}
