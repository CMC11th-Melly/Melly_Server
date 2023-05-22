package cmc.mellyserver.auth.application.dto.response;

import cmc.mellyserver.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * OAuthLoginResponseDto.java
 *
 * @author jemlog
 */
@Data
@AllArgsConstructor
@Builder
public class OAuthLoginResponseDto {
    private String accessToken;
    private Boolean isNewUser;
    private User user;
}
