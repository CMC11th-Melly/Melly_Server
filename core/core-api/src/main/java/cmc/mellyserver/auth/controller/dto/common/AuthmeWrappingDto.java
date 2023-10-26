package cmc.mellyserver.auth.controller.dto.common;

import cmc.mellyserver.auth.controller.dto.response.AccessTokenUserData;
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
