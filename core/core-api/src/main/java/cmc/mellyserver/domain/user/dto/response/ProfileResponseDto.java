package cmc.mellyserver.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponseDto implements Serializable {

	private Long userId;

	private String nickname;

	private String email;

	private String profileImage;

	public static ProfileResponseDto of(Long userId, String nickname, String email, String profileImage) {
		return new ProfileResponseDto(userId, nickname, email, profileImage);
	}

}
