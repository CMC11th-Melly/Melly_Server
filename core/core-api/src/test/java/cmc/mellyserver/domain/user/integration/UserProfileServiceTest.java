package cmc.mellyserver.domain.user.integration;

import static fixtures.UserFixtures.*;
import static org.assertj.core.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.UserRepository;
import cmc.mellyserver.domain.user.UserProfileService;
import cmc.mellyserver.domain.user.dto.response.ProfileResponseDto;
import cmc.mellyserver.domain.user.dto.response.ProfileUpdateRequestDto;
import cmc.mellyserver.support.IntegrationTestSupport;

public class UserProfileServiceTest extends IntegrationTestSupport {

	@Autowired
	private UserProfileService userProfileService;

	@Autowired
	private UserRepository userRepository;

	@DisplayName("유저 프로필을 조회한다")
	@Test
	void 유저_프로필을_조회한다() {

		// Given
		User 모카 = userRepository.save(모카());

		// When
		ProfileResponseDto userProfile = userProfileService.getProfile(모카.getId());

		// Then
		assertThat(userProfile.getUserId()).isEqualTo(모카.getId());
		assertThat(userProfile.getNickname()).isEqualTo(모카.getNickname());
	}

	@DisplayName("수정하려는 프로필 이미지가 있으면 변경한다.")
	@Test
	void 유저_프로필_이미지를_수정한다() throws IOException {

		// given
		User savedUser = userRepository.save(모카());

		// when
		userProfileService.updateProfileImage(savedUser.getId(), new MockMultipartFile(모카_프로필, 모카_프로필.getBytes()),
			false);

		// then
		User user = userRepository.findById(savedUser.getId()).get();
		assertThat(user.getProfileImage()).isEqualTo("수정된프로필.jpg");
	}

	@DisplayName("유저 프로필 정보를 수정한다.")
	@Test
	void 유저_프로필_정보를_수정한다() {

		// given
		User 모카 = userRepository.save(모카());
		ProfileUpdateRequestDto 모카_프로필_수정_요청 = ProfileUpdateRequestDto.builder().nickname("지원").build();

		// when
		userProfileService.updateProfile(모카.getId(), 모카_프로필_수정_요청);

		// then
		User updatedUser = userRepository.findById(모카.getId()).get();
		assertThat(updatedUser.getNickname()).isEqualTo("지원");
	}

}
