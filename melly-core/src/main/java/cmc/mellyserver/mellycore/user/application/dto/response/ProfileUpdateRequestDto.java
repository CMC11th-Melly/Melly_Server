package cmc.mellyserver.mellycore.user.application.dto.response;

import cmc.mellyserver.mellycommon.enums.AgeGroup;
import cmc.mellyserver.mellycommon.enums.Gender;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Data
@Getter
public class ProfileUpdateRequestDto {

    private Long id;
    private MultipartFile profileImage;
    private String nickname;
    private Gender gender;
    private AgeGroup ageGroup;
    private Boolean isBasicProfile;

    @Builder
    public ProfileUpdateRequestDto(Long id, MultipartFile profileImage, String nickname, Gender gender,
                                   AgeGroup ageGroup, Boolean isBasicProfile) {
        this.id = id;
        this.profileImage = profileImage;
        this.nickname = nickname;
        this.gender = gender;
        this.ageGroup = ageGroup;
        this.isBasicProfile = isBasicProfile;
    }
}
