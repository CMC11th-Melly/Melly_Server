package cmc.mellyserver.auth.application.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * AuthLoginDto.java
 *
 * @author jemlog
 */
@Data
@AllArgsConstructor
public class AuthLoginRequestDto {

    private String email;

    private String password;

    private String fcmToken;
}
