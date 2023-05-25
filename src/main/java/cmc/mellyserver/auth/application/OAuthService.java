package cmc.mellyserver.auth.application;


import cmc.mellyserver.auth.application.dto.response.OAuthLoginResponseDto;
import cmc.mellyserver.auth.presentation.dto.request.OAuthLoginRequest;

public interface OAuthService {

    OAuthLoginResponseDto login(OAuthLoginRequest oAuthLoginRequest);
}
