package cmc.mellyserver.controller.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {

  private String uid;

  private String profileImage;

  private String nickname;

}
