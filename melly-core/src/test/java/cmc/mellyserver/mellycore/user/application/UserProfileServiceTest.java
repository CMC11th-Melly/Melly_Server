package cmc.mellyserver.mellycore.user.application;


import cmc.mellyserver.mellycore.builder.GivenBuilder;
import cmc.mellyserver.mellycore.config.IntegrationTest;
import cmc.mellyserver.mellycore.user.application.dto.response.ProfileResponseDto;
import cmc.mellyserver.mellycore.user.application.dto.response.ProfileUpdateRequestDto;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.enums.AgeGroup;
import cmc.mellyserver.mellycore.user.domain.enums.Gender;
import cmc.mellyserver.mellycore.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static cmc.mellyserver.mellycore.common.fixture.UserFixtures.머식_프로필;
import static org.assertj.core.api.Assertions.assertThat;

public class UserProfileServiceTest extends IntegrationTest {

    private final ProfileUpdateRequestDto 모카_프로필_수정_요청 = new ProfileUpdateRequestDto("머식", Gender.FEMALE, AgeGroup.THREE);
    private final MockMultipartFile 모카_프로필_사진_수정_요청 = new MockMultipartFile(머식_프로필, 머식_프로필.getBytes());

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
        ProfileResponseDto userProfile = userProfileService.getUserProfile(모카.회원().getId());

        assertThat(userProfile.getUserId()).isEqualTo(모카.회원().getId());
        assertThat(userProfile.getNickname()).isEqualTo("모카");
    }

    @DisplayName("수정하려는 프로필 이미지가 있으면 변경한다.")
    @Test
    void 유저_프로필_이미지를_수정한다() throws IOException {

        // given
        GivenBuilder 모카 = 모카();

        // when
        userProfileService.updateUserProfileImage(모카.회원().getId(), 모카_프로필_사진_수정_요청);

        // then
        User updatedUser = userRepository.getById(모카.회원().getId());
        assertThat(updatedUser.getProfileImage()).isEqualTo(머식_프로필);
    }

    @DisplayName("수정하려는 프로필 이미지가 없으면 기본 이미지로 변경한다.")
    @Test
    void 유저_프로필_이미지를_기본_이미지로_변경한다() throws IOException {

        // given
        GivenBuilder 모카 = 모카();

        // when
        userProfileService.updateUserProfileImage(모카.회원().getId(), null);

        // then
        User updatedUser = userRepository.getById(모카.회원().getId());
        assertThat(updatedUser.getProfileImage()).isNull();
    }

    @DisplayName("유저 프로필 정보를 수정한다.")
    @Test
    void 유저_프로필_정보를_수정한다() {

        // given
        GivenBuilder 모카 = 모카();

        // when
        userProfileService.updateUserProfile(모카.회원().getId(), 모카_프로필_수정_요청);

        // then
        User updatedUser = userRepository.getById(모카.회원().getId());
        assertThat(updatedUser.getNickname()).isEqualTo("머식");
    }
}


