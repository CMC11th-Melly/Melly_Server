package cmc.mellyserver.auth.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OAuthLoginResponse {
    private String accessToken;
    private Boolean isSignup;
}
