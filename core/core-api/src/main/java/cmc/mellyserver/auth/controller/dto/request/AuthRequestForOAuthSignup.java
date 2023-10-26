package cmc.mellyserver.auth.controller.dto.request;

import cmc.mellyserver.dbcore.user.enums.AgeGroup;
import cmc.mellyserver.dbcore.user.enums.Gender;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthRequestForOAuthSignup {

	private String uid;

	private String nickname;

	private Gender gender;

	@Nullable
	private MultipartFile profileImage;

	@Nullable
	private AgeGroup ageGroup;

	@Nullable
	private String fcmToken;

}
