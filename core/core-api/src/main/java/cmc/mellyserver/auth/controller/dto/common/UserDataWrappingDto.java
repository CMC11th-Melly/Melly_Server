package cmc.mellyserver.auth.controller.dto.common;

import cmc.mellyserver.auth.controller.dto.response.AccessTokenUserData;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserDataWrappingDto {

	private AccessTokenUserData user;

}
