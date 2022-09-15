package cmc.mellyserver.auth.application;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OAuthLoginResponseDto {

    private String accessToken;
    private Boolean isSignup;
}
