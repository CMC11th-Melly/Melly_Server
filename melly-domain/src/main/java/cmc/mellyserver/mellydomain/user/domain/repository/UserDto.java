package cmc.mellyserver.mellydomain.user.domain.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long userId;
    private String profileImage;
    private String nickname;
    private Boolean isLoginUser;

}
