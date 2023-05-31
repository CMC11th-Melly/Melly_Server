package cmc.mellyserver.auth.application.dto.response;

import cmc.mellyserver.auth.presentation.dto.response.AccessTokenUserData;
import cmc.mellyserver.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
public class SignupResponseDto {

    private AccessTokenUserData user;
    private String token;

    @Builder
    public SignupResponseDto(AccessTokenUserData user, String token) {
        this.user = user;
        this.token = token;
    }

    public static SignupResponseDto of(User user, String token)
    {
        return SignupResponseDto.builder()
                .user(AccessTokenUserData.from(user))
                .token(token)
                .build();
    }
}