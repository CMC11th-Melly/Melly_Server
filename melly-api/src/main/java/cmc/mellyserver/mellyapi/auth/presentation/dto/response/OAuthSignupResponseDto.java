package cmc.mellyserver.mellyapi.auth.presentation.dto.response;

import cmc.mellyserver.mellycore.user.domain.enums.Provider;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OAuthSignupResponseDto {

    private String email;

    private String socialId;

    private Provider provider;
}
