package cmc.mellyserver.auth.presentation.dto;

import cmc.mellyserver.user.domain.AgeGroup;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthUserDataResponse {

    private int code;
    private String message;
    private AccessTokenUserData user;

}
