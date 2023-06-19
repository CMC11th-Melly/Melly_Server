package cmc.mellyserver.mellyappexternalapi.user.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDto {

	private String uid;
	private String profileImage;
	private String nickname;
}
