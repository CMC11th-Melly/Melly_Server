package cmc.mellyserver.mellyappexternalapi.user.application.dto.response;

import org.springframework.web.multipart.MultipartFile;

import cmc.mellyserver.mellydomain.common.enums.AgeGroup;
import cmc.mellyserver.mellydomain.common.enums.Gender;
import lombok.Builder;
import lombok.Data;

@Data
public class ProfileUpdateRequestDto {

	private Long userSeq;
	private MultipartFile profileImage;
	private String nickname;
	private Gender gender;
	private AgeGroup ageGroup;
	private boolean deleteImage;

	@Builder
	public ProfileUpdateRequestDto(Long userSeq, MultipartFile profileImage, String nickname, Gender gender,
		AgeGroup ageGroup, boolean deleteImage) {
		this.userSeq = userSeq;
		this.profileImage = profileImage;
		this.nickname = nickname;
		this.gender = gender;
		this.ageGroup = ageGroup;
		this.deleteImage = deleteImage;
	}
}
