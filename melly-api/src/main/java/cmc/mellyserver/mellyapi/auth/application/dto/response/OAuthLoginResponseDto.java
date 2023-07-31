package cmc.mellyserver.mellyapi.auth.application.dto.response;

import cmc.mellyserver.mellycommon.enums.UserStatus;
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
    private UserStatus userStatus;
}
