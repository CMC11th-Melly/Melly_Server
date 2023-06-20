package cmc.mellyserver.mellyappexternalapi.user.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {

    private String uid;
    private String profileImage;
    private String nickname;
}
