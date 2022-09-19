package cmc.mellyserver.auth.presentation.dto;

import cmc.mellyserver.user.domain.AgeGroup;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthUserDataResponse {
    private Long userSeq;
    private String uid;
    private Provider provider;
    private String email;
    private String nickname;
    private String profileImage;
    private boolean gender;
    private AgeGroup ageGroup;
}
