package cmc.mellyserver.controller.auth.dto.request;

import cmc.mellyserver.dbcore.user.enums.AgeGroup;
import cmc.mellyserver.dbcore.user.enums.Gender;
import cmc.mellyserver.domain.auth.dto.request.AuthSignupRequestDto;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonSignupRequest {

    @Email(message = "이메일 양식으로 작성해주세요.")
    private String email; // 이메일

    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z])[0-9a-zA-Z]{8,}",
            message = "비밀번호는 영문과 숫자 조합으로 8자리 이상 가능합니다.")
    private String password; // 비밀번호

    @Length(min = 2, max = 15, message = "닉네임은 2자 이상 15자 이하로 작성해주세요.")
    private String nickname; // 닉네임


    private Gender gender; // 성별


    private AgeGroup ageGroup; // 연령대

    private String fcmToken;

    public AuthSignupRequestDto toDto() {
        return new AuthSignupRequestDto(email, password, nickname, ageGroup, gender, fcmToken);
    }
}
