package cmc.mellyserver.auth.presentation.dto.response;

import cmc.mellyserver.user.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
public class LoginResponse {

    private AccessTokenUserData user;
    private String token;

    @Builder
    public LoginResponse(AccessTokenUserData user, String token) {
        this.user = user;
        this.token = token;
    }
}
