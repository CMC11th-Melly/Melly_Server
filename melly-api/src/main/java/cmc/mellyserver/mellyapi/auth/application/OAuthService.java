package cmc.mellyserver.mellyapi.auth.application;

import cmc.mellyserver.mellyapi.auth.application.dto.request.OAuthLoginRequestDto;
import cmc.mellyserver.mellyapi.auth.application.dto.response.OAuthLoginResponseDto;

public interface OAuthService {

    OAuthLoginResponseDto login(OAuthLoginRequestDto oAuthLoginRequestDto);
}
