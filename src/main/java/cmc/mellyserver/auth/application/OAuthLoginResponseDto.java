package cmc.mellyserver.auth.application;

import cmc.mellyserver.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@AllArgsConstructor
public class OAuthLoginResponseDto {

    private String accessToken;
    private Boolean isNewUser;
    private User user;

}
