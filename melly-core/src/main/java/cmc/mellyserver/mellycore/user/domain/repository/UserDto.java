package cmc.mellyserver.mellycore.user.domain.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {

    private Long userId;
    private String profileImage;
    private String nickname;
}
