package cmc.mellyserver.domain.group.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMemberResponseDto implements Serializable {

  private Long userId;

  private String profileImage;

  private String nickname;

  private boolean isCurrentUser;

  public static GroupMemberResponseDto of(Long userId, String profileImage, String nickname, boolean isCurrentUser) {
	return new GroupMemberResponseDto(userId, profileImage, nickname, isCurrentUser);
  }

}
