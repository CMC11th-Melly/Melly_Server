package cmc.mellyserver.mellyapi.auth.presentation.dto.request;

import cmc.mellyserver.mellyapi.auth.application.dto.request.AuthLoginRequestDto;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthLoginRequest {

	private String email;

	private String password;

	private String fcmToken;

	public AuthLoginRequestDto toServiceDto() {
		return new AuthLoginRequestDto(email, password, fcmToken);
	}

}