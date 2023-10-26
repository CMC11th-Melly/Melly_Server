package cmc.mellyserver.domain.user.application;

import cmc.mellyserver.builder.GivenBuilder;
import cmc.mellyserver.common.fixture.UserFixtures;
import cmc.mellyserver.config.IntegrationTest;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.UserRepository;
import cmc.mellyserver.dbcore.user.enums.AgeGroup;
import cmc.mellyserver.dbcore.user.enums.Gender;
import cmc.mellyserver.domain.user.UserProfileService;
import cmc.mellyserver.domain.user.dto.response.ProfileResponseDto;
import cmc.mellyserver.domain.user.dto.response.ProfileUpdateRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static cmc.mellyserver.common.fixture.UserFixtures.모카_프로필;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class UserProfileServiceTest extends IntegrationTest {

	private static final ProfileUpdateRequestDto 모카_프로필_수정_요청 = new ProfileUpdateRequestDto("머식", Gender.FEMALE,
			AgeGroup.THREE);

	@Autowired
	private UserProfileService userProfileService;

	@Autowired
	private UserRepository userRepository;

	@DisplayName("유저 프로필을 조회한다.")
	@Test
	void 유저_프로필을_조회한다() {

		// given
		GivenBuilder 모카 = 모카();

		// when & then
		ProfileResponseDto userProfile = userProfileService.getProfile(모카.회원().getId());

		assertThat(userProfile.getUserId()).isEqualTo(모카.회원().getId());
		assertThat(userProfile.getNickname()).isEqualTo("모카");
	}

	@DisplayName("수정하려는 프로필 이미지가 있으면 변경한다.")
	@Test
	void 유저_프로필_이미지를_수정한다() throws IOException {

		// given
		User 모카 = UserFixtures.모카();
		User savedUser = userRepository.save(모카);

		// when
		userProfileService.updateProfileImage(savedUser.getId(), new MockMultipartFile(모카_프로필, 모카_프로필.getBytes()));

		// then
		User user = userRepository.findById(savedUser.getId()).get();
		assertThat(user.getProfileImage()).isEqualTo("mock.jpg");
	}

	@DisplayName("유저 프로필 정보를 수정한다.")
	@Test
	void 유저_프로필_정보를_수정한다() {

		// given
		GivenBuilder 모카 = 모카();

		// when
		userProfileService.updateProfile(모카.회원().getId(), 모카_프로필_수정_요청);

		// then
		User updatedUser = userRepository.findById(모카.회원().getId()).get();
		assertThat(updatedUser.getNickname()).isEqualTo("머식");
	}

}
