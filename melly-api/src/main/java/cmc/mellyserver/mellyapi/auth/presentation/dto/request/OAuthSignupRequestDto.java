package cmc.mellyserver.mellyapi.auth.presentation.dto.request;

import cmc.mellyserver.mellycore.user.domain.enums.AgeGroup;
import cmc.mellyserver.mellycore.user.domain.enums.Gender;
import cmc.mellyserver.mellycore.user.domain.enums.Provider;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OAuthSignupRequestDto {

    private String email;

    private String socialId;

    private Provider provider;

    private String nickname;


    private Gender gender;


    private AgeGroup ageGroup;

    private String fcmToken;
}
