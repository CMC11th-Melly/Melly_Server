package cmc.mellyserver.controller.auth.dto.response;

import cmc.mellyserver.dbcore.user.enums.Provider;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OAuthSignupResponseDto {

    private String email;

    private String socialId;

    private Provider provider;
}
