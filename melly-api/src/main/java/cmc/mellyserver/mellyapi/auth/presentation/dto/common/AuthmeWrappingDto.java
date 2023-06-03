package cmc.mellyserver.mellyapi.auth.presentation.dto.common;

import cmc.mellyserver.mellyapi.auth.presentation.dto.response.AccessTokenUserData;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthmeWrappingDto {
	private AccessTokenUserData user;
}
