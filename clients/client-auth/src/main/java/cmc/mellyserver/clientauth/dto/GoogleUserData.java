package cmc.mellyserver.clientauth.dto;

import cmc.mellyserver.clientauth.api.LoginClientResult;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GoogleUserData {

    private String sub;

    private String email;

    private Boolean email_verified;

    public LoginClientResult toResult() {
        return new LoginClientResult(sub, "google");
    }
}
