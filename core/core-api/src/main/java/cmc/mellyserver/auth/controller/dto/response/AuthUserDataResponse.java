package cmc.mellyserver.auth.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthUserDataResponse {

	private int code;

	private String message;

	private AccessTokenUserData user;

}
