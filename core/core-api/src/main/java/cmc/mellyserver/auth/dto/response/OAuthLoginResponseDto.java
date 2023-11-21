package cmc.mellyserver.auth.dto.response;

import cmc.mellyserver.dbcore.user.enums.UserStatus;

public record OAuthLoginResponseDto(String accessToken, UserStatus userStatus) {

}
