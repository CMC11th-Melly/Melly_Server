package cmc.mellyserver.auth.presentation.dto;

import cmc.mellyserver.user.domain.AgeGroup;
import cmc.mellyserver.user.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
