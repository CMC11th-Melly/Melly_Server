package cmc.mellyserver.auth.service.dto.response;

import cmc.mellyserver.dbcore.user.enums.UserStatus;

public record OAuthLoginResponseDto(String accessToken, UserStatus userStatus) {

}
