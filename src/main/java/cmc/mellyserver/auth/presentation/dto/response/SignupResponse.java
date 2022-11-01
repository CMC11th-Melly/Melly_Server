package cmc.mellyserver.auth.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupResponse {

    private AccessTokenUserData user;
    private String token;
}
