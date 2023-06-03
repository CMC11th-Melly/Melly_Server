package cmc.mellyserver.mellyapi.auth.application;

import cmc.mellyserver.mellyapi.auth.application.dto.request.AuthLoginRequestDto;
import cmc.mellyserver.mellyapi.auth.application.dto.request.AuthSignupRequestDto;
import cmc.mellyserver.mellyapi.auth.application.dto.response.LoginResponseDto;
import cmc.mellyserver.mellyapi.auth.application.dto.response.SignupResponseDto;

public interface AuthService {

	SignupResponseDto signup(AuthSignupRequestDto authSignupRequestDto);

	LoginResponseDto login(AuthLoginRequestDto authLoginRequestDto);

	void logout(Long loginUserSeq, String accessToken);

	void checkDuplicatedNickname(String nickname);

	void checkDuplicatedEmail(String email);

	void withdraw(Long loginUserSeq, String accessToken);
}
