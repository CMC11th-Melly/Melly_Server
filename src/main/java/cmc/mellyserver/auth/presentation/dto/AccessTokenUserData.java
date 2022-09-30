package cmc.mellyserver.auth.presentation.dto;

import cmc.mellyserver.user.domain.enums.AgeGroup;
import cmc.mellyserver.user.domain.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AccessTokenUserData {
    private Long userSeq;
    private String uid;
    private Provider provider;
    private String email;
    private String nickname;
    private String profileImage;
    private Gender gender;
    private AgeGroup ageGroup;
}
