package cmc.mellyserver.auth.dto.request;

import lombok.Builder;
import lombok.Data;

/**
 * AuthLoginDto.java
 *
 * @author jemlog
 */
@Data
public class AuthLoginRequestDto {

	private String email;

	private String password;

	private String fcmToken;

	@Builder
	public AuthLoginRequestDto(String email, String password, String fcmToken) {
		this.email = email;
		this.password = password;
		this.fcmToken = fcmToken;
	}

}
