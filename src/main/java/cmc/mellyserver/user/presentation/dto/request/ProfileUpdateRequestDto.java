package cmc.mellyserver.user.presentation.dto.request;

import cmc.mellyserver.common.enums.AgeGroup;
import cmc.mellyserver.common.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProfileUpdateRequestDto {

    private Long userSeq;
    private MultipartFile profileImage;
    private String nickname;
    private Gender gender;
    private AgeGroup ageGroup;
    private boolean deleteImage;

    @Builder
    public ProfileUpdateRequestDto(Long userSeq, MultipartFile profileImage, String nickname, Gender gender, AgeGroup ageGroup, boolean deleteImage) {
        this.userSeq = userSeq;
        this.profileImage = profileImage;
        this.nickname = nickname;
        this.gender = gender;
        this.ageGroup = ageGroup;
        this.deleteImage = deleteImage;
    }

    public static ProfileUpdateRequestDto of(Long userSeq, ProfileUpdateRequest profileUpdateRequest)
    {
        return ProfileUpdateRequestDto.builder()
                .userSeq(userSeq)
                .profileImage(profileUpdateRequest.getProfileImage())
                .nickname(profileUpdateRequest.getNickname())
                .gender(profileUpdateRequest.getGender())
                .ageGroup(profileUpdateRequest.getAgeGroup())
                .deleteImage(profileUpdateRequest.isDeleteImage())
                .build();
    }
}
