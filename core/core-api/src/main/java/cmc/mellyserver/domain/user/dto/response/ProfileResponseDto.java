package cmc.mellyserver.domain.user.dto.response;

import java.io.Serializable;

import cmc.mellyserver.dbcore.user.User;

public record ProfileResponseDto(Long userId, String nickname, String email, String profileImage)
    implements Serializable {

    public static ProfileResponseDto of(User user) {
        return new ProfileResponseDto(user.getId(), user.getNickname(), user.getEmail(), user.getProfileImage());
    }

}
