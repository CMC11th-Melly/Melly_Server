package cmc.mellyserver.mellyappexternalapi.auth.application;

import cmc.mellyserver.mellyappexternalapi.auth.application.dto.request.OAuthLoginRequestDto;
import cmc.mellyserver.mellyappexternalapi.auth.application.dto.response.OAuthLoginResponseDto;

public interface OAuthService {

    OAuthLoginResponseDto login(OAuthLoginRequestDto oAuthLoginRequestDto);
}
