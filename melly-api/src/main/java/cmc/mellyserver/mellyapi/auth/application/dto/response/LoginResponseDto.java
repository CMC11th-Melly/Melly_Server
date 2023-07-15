package cmc.mellyserver.mellyapi.auth.application.dto.response;

import cmc.mellyserver.mellyapi.auth.presentation.dto.response.AccessTokenUserData;
import cmc.mellyserver.mellycore.user.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
public class LoginResponseDto {

    private AccessTokenUserData user;
    private String token;

    @Builder
    public LoginResponseDto(AccessTokenUserData user, String token) {
        this.user = user;
        this.token = token;
    }

    public static LoginResponseDto of(User user, String token) {
        return LoginResponseDto.builder()
                .user(AccessTokenUserData.from(user))
                .token(token)
                .build();
    }
}