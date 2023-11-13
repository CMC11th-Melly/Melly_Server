package cmc.mellyserver.auth.dto.response;

import cmc.mellyserver.dbcore.user.enums.UserStatus;
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
