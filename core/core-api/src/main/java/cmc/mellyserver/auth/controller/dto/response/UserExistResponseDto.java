package cmc.mellyserver.auth.controller.dto.response;

import cmc.mellyserver.dbcore.user.enums.Provider;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserExistResponseDto {

	private Provider provider;

	private String email;

}
