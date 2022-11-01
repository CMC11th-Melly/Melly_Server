package cmc.mellyserver.user.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDto {

    private String uid;
    private String profileImage;
    private String nickname;
}
