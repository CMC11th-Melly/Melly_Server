package cmc.mellyserver.domain.user.dto.response;

import java.io.Serializable;

import cmc.mellyserver.dbcore.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponseDto implements Serializable {

  private Long userId;

  private String nickname;

  private String email;

  private String profileImage;

  public static ProfileResponseDto of(User user) {
	return new ProfileResponseDto(user.getId(), user.getNickname(), user.getEmail(), user.getProfileImage());
  }

}
