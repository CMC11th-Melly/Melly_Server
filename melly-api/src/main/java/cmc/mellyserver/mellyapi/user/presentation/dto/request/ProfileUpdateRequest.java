package cmc.mellyserver.mellyapi.user.presentation.dto.request;

import org.springframework.web.multipart.MultipartFile;

import cmc.mellyserver.mellycore.common.enums.AgeGroup;
import cmc.mellyserver.mellycore.common.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ProfileUpdateRequest {

	private MultipartFile profileImage;

	private String nickname;

	private Gender gender;

	private AgeGroup ageGroup;

	private boolean deleteImage;

}
