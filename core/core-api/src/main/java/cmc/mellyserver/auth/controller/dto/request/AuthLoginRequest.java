package cmc.mellyserver.auth.controller.dto.request;

import cmc.mellyserver.auth.dto.request.AuthLoginRequestDto;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthLoginRequest {

  @Email(message = "이메일 형식을 지켜야 합니다")
  private String email;

  private String password;

  private String fcmToken;

  public AuthLoginRequest(String email, String password) {
	this.email = email;
	this.password = password;
  }

  public AuthLoginRequestDto toDto() {
	return new AuthLoginRequestDto(email, password, fcmToken);
  }

}