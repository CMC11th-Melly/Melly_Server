package cmc.mellyserver.auth.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OAuthLoginResponse {

    private AccessTokenUserData user;
    private String token;
    private boolean isNewUser;

    public OAuthLoginResponse(boolean isNewUser, AccessTokenUserData user) {
        this.isNewUser = isNewUser;
        this.user = AccessTokenUserData.builder().uid(user.getUid()).build();
    }
}
