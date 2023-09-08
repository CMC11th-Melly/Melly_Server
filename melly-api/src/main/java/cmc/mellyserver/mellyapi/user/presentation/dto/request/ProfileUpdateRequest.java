package cmc.mellyserver.mellyapi.user.presentation.dto.request;

import cmc.mellyserver.mellycore.user.application.dto.response.ProfileUpdateRequestDto;
import cmc.mellyserver.mellycore.user.domain.enums.AgeGroup;
import cmc.mellyserver.mellycore.user.domain.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateRequest {

    @Size(min = 2, max = 15, message = "닉네임은 2자 이상 15자 이하로 입력해야 합니다.")
    private String nickname;

    @NotEmpty(message = "성별을 입력해야 합니다.")
    private String gender;

    @NotEmpty(message = "연령대를 입력해야 합니다.")
    private String ageGroup;

    public ProfileUpdateRequestDto toServiceRequest() {
        return new ProfileUpdateRequestDto(nickname, Gender.from(gender), AgeGroup.from(ageGroup));
    }
}
