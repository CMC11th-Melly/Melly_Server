package cmc.mellyserver.mellyapi.user.presentation.dto.request;

import cmc.mellyserver.mellycommon.enums.AgeGroup;
import cmc.mellyserver.mellycommon.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileUpdateRequest {

    @Length(min = 2, max = 15, message = "닉네임은 2자 이상 15자 이하로 입력해야 합니다.")
    private String nickname;

    @NotNull(message = "성별을 입력하셔야 합니다.")
    private Gender gender;

    @NotNull(message = "연령대를 선택하셔야 합니다.")
    private AgeGroup ageGroup;

    private Boolean isBasicProfile;

    // case 1 : 이미지 그대로 두기 -> multipart is empty & 기본 이미지 flag = false
    // case 2 : 기본이미지로 변경  -> multipart is empty & 기본 이미지 flag = true
    // case 3 : 새로운 이미지로 변경 -> multipart is not empty & 기본 이미지 flag = false

    // 1차적으로 multipart가 empty인지 아닌지 판별

}
