package cmc.mellyserver.user.presentation.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {

    private Long userId;
    private String profileImage;
    private String nickname;
    private Boolean isLoginUser;


}
