package cmc.mellyserver.mellyappexternalapi.auth.presentation.dto.request;

import cmc.mellyserver.mellyappexternalapi.auth.application.dto.request.AuthLoginRequestDto;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthLoginRequest {

    @Email
    private String email;

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).{8,}$")
    private String password;

    private String fcmToken;

    public AuthLoginRequestDto toServiceDto() {
        return new AuthLoginRequestDto(email, password, fcmToken);
    }

    public AuthLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}