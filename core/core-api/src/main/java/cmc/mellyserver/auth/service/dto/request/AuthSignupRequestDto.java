package cmc.mellyserver.auth.service.dto.request;

import org.hibernate.validator.constraints.Length;

import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.enums.AgeGroup;
import cmc.mellyserver.dbcore.user.enums.Gender;
import cmc.mellyserver.dbcore.user.enums.Provider;
import cmc.mellyserver.dbcore.user.enums.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

public record AuthSignupRequestDto(

    @Email
    String email,

    @Length(min = 2, max = 15, message = "닉네임은 2자 이상 15자 이하로 작성해주세요.")
    String nickname,

    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z])[0-9a-zA-Z]{8,}", message = "비밀번호는 영문과 숫자 조합으로 8자리 이상 가능합니다.")
    String password,

    Gender gender,

    AgeGroup ageGroup,

    String fcmToken
) {
    @Builder
    public AuthSignupRequestDto {
    }

    public User toEntity() {
        return User.builder()
            .email(email)
            .password(password)
            .nickname(nickname)
            .ageGroup(ageGroup)
            .gender(gender)
            .fcmToken(fcmToken)
            .roleType(
                RoleType.USER)
            .provider(Provider.DEFAULT)
            .build();
    }

}
