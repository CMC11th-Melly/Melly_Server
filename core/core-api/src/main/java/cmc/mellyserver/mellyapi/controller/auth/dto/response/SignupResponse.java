package cmc.mellyserver.mellyapi.controller.auth.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class SignupResponse {

    private AccessTokenUserData user;
    private String token;

    @Builder
    public SignupResponse(AccessTokenUserData user, String token) {
        this.user = user;
        this.token = token;
    }
}
