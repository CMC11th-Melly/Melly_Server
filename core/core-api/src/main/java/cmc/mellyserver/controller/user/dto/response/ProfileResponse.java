package cmc.mellyserver.controller.user.dto.response;

import cmc.mellyserver.domain.user.dto.response.ProfileResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileResponse {

  private Long userId;

  private String nickname;

  private String profileImage;

  private Integer imageVolume;

  public static ProfileResponse of(ProfileResponseDto profileResponseDto, Integer imageVolume) {
	return new ProfileResponse(profileResponseDto.getUserId(), profileResponseDto.getNickname(),
		profileResponseDto.getProfileImage(), imageVolume);
  }

}
