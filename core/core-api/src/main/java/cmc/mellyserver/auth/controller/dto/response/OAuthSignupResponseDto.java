package cmc.mellyserver.auth.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OAuthSignupResponseDto {

    private String socialId;

    private String provider;

}
