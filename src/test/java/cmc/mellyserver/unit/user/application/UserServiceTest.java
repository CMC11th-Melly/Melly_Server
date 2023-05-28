package cmc.mellyserver.unit.user.application;

import cmc.mellyserver.common.enums.*;
import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.factory.UserFactory;
import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.common.util.aws.impl.AwsServiceImpl;
import cmc.mellyserver.common.util.aws.impl.S3FileLoader;
import cmc.mellyserver.group.application.GroupService;
import cmc.mellyserver.group.domain.repository.UserGroupQueryRepository;
import cmc.mellyserver.memory.domain.repository.MemoryQueryRepository;
import cmc.mellyserver.memory.domain.repository.dto.MemoryResponseDto;
import cmc.mellyserver.user.application.dto.response.GroupLoginUserParticipatedResponseDto;
import cmc.mellyserver.user.application.dto.response.ProfileUpdateFormResponseDto;
import cmc.mellyserver.user.application.dto.response.SurveyRecommendResponseDto;
import cmc.mellyserver.user.application.impl.UserServiceImpl;
import cmc.mellyserver.user.domain.Recommend;
import cmc.mellyserver.user.domain.User;
import cmc.mellyserver.user.domain.repository.UserRepository;
import cmc.mellyserver.user.infrastructure.SurveyRecommender;
import cmc.mellyserver.user.presentation.dto.common.UserDto;
import cmc.mellyserver.user.presentation.dto.request.ProfileUpdateRequestDto;
import cmc.mellyserver.user.presentation.dto.request.SurveyRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private AuthenticatedUserChecker authenticatedUserChecker;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MemoryQueryRepository memoryQueryRepository;

    @Mock
    private UserGroupQueryRepository userGroupQueryRepository;

    @Mock
    private GroupService groupService;

    @Mock
    private S3FileLoader s3FileLoader;

    @Mock
    private AwsServiceImpl awsService;

    @Mock
    private SurveyRecommender surveyRecommender;


    @DisplayName("처음 회원가입을 했을때")
    @Nested
    class When_Signup {

        @DisplayName("사용자의 취향에 따라 설문 조사를 진행한다.")
        @Test
        void create_survey() {

            // given
            User emailLoginUser = UserFactory.createEmailLoginUser();
            SurveyRequestDto surveyRequestDto = UserFactory.mockSurveyRequestDto();

            given(authenticatedUserChecker.checkAuthenticatedUserExist(surveyRequestDto.getUserSeq()))
                    .willReturn(emailLoginUser);

            // when
            userService.createSurvey(surveyRequestDto);

            // then
            assertThat(emailLoginUser.getRecommend()).usingRecursiveComparison()
                    .isEqualTo(new Recommend(RecommendGroup.FRIEND, RecommendPlace.PLACE1, RecommendActivity.CAFE));
        }

        @DisplayName("사용자의 설문 조사 결과에 따라 장소를 추천받을 수 있다.")
        @Test
        void getSurveyResult_Reccommend_Place() {

            // given
            User emailLoginUser = UserFactory.createEmailLoginUser();
            Long loginUserSeq = emailLoginUser.getUserSeq();
            SurveyRecommendResponseDto surveyRecommendResponseDto = UserFactory.mockSurveyRecommendResponseDto();

            given(authenticatedUserChecker.checkAuthenticatedUserExist(loginUserSeq))
                    .willReturn(emailLoginUser);

            given(surveyRecommender.getRecommend(emailLoginUser))
                    .willReturn(surveyRecommendResponseDto);

            // when
            SurveyRecommendResponseDto surveyResult = userService.getSurveyResult(loginUserSeq);

            // then
            assertThat(surveyResult)
                    .usingRecursiveComparison()
                    .isEqualTo(surveyRecommendResponseDto);

            verify(surveyRecommender, times(1))
                    .getRecommend(any(User.class));
        }
    }

    @DisplayName("사용자는 그룹에 추가될 수 있다.")
    @Test
    void participate_group() {

        // given
        Mockito.doNothing().when(groupService).participateToGroup(anyLong(), anyLong());

        // when
        userService.participateToGroup(1L, 1L);

        // then
        verify(groupService, times(1))
                .participateToGroup(anyLong(), anyLong());
    }

    @DisplayName("로그인한 사용자가 속해있는 그룹 리스트를 조회")
    @Test
    void find_group_list_login_user_participated() {

        // given
        List<GroupLoginUserParticipatedResponseDto> groupLoginUserParticipatedResponseDtos = List.of(
                new GroupLoginUserParticipatedResponseDto(1L, 1, "친구", List.of(new UserDto(1L, "testImage.png", "jemin", true), new UserDto(2L, "testImage.png", "jemin2", false)), GroupType.FRIEND, "link"),
                new GroupLoginUserParticipatedResponseDto(2L, 2, "동료", List.of(new UserDto(1L, "testImage.png", "jemin", false), new UserDto(2L, "testImage.png", "jemin2", true)), GroupType.COMPANY, "link")
        );

        given(userGroupQueryRepository.getGroupListLoginUserParticipate(anyLong()))
                .willReturn(groupLoginUserParticipatedResponseDtos);

        // when
        List<GroupLoginUserParticipatedResponseDto> groupListLoginUserParticiated = userService.findGroupListLoginUserParticiated(anyLong());

        // then
        assertThat(groupListLoginUserParticiated).usingRecursiveComparison()
                .isEqualTo(groupListLoginUserParticiated);

        verify(userGroupQueryRepository, times(1))
                .getGroupListLoginUserParticipate(anyLong());
    }

    @DisplayName("로그인한 사용자가 작성한 메모리 리스트 조회")
    @Test
    void findMemoriesLoginUserWrite() {

        // given
        List<MemoryResponseDto> responseDtos = List.of(
                new MemoryResponseDto(1L, "테스트 장소", 2L, "다음에 또 가고싶다!", "친구랑 오기 좋아요", GroupType.FRIEND, "동기들", 5L, true, LocalDateTime.of(2023, 5, 30, 20, 30)),
                new MemoryResponseDto(2L, "두번째 장소", 3L, "내일 또 가요", "동료랑 오기 좋아요", GroupType.COMPANY, "동료들", 4L, true, LocalDateTime.of(2023, 5, 29, 20, 30))
        );

        Slice<MemoryResponseDto> memoryResponseDtos = new SliceImpl<>(responseDtos, PageRequest.of(0, 2), false);

        given(memoryQueryRepository.searchMemoryUserCreatedForMyPage(any(Pageable.class), anyLong(), any(GroupType.class)))
                .willReturn(memoryResponseDtos);

        // when
        Slice<MemoryResponseDto> memoriesLoginUserWrite = userService.findMemoriesLoginUserWrite(PageRequest.of(0, 2), 1L, GroupType.FRIEND);

        // then
        assertThat(memoriesLoginUserWrite).usingRecursiveComparison()
                .isEqualTo(memoryResponseDtos);

        verify(memoryQueryRepository, times(1))
                .searchMemoryUserCreatedForMyPage(any(Pageable.class), anyLong(), any(GroupType.class));
    }


    @DisplayName("로그인한 사용자와 같은 그룹의 사람이 작성한 메모리 리스트 조회")
    @Test
    void findMemoriesUsersBelongToMyGroupWrite() {

        // given
        List<MemoryResponseDto> responseDtos = List.of(
                new MemoryResponseDto(1L, "테스트 장소", 2L, "다음에 또 가고싶다!", "친구랑 오기 좋아요", GroupType.FRIEND, "동기들", 5L, true, LocalDateTime.of(2023, 5, 30, 20, 30)),
                new MemoryResponseDto(2L, "두번째 장소", 3L, "내일 또 가요", "동료랑 오기 좋아요", GroupType.COMPANY, "동료들", 4L, true, LocalDateTime.of(2023, 5, 29, 20, 30))
        );

        Slice<MemoryResponseDto> memoryResponseDtos = new SliceImpl<>(responseDtos, PageRequest.of(0, 2), false);

        given(userGroupQueryRepository.getMyGroupMemory(any(Pageable.class), anyLong(), anyLong()))
                .willReturn(memoryResponseDtos);

        // when
        Slice<MemoryResponseDto> memoriesUsersBelongToMyGroupWrite = userService.findMemoriesUsersBelongToMyGroupWrite(PageRequest.of(0, 2), 1L, 1L);

        // then
        assertThat(memoriesUsersBelongToMyGroupWrite).usingRecursiveComparison()
                .isEqualTo(memoryResponseDtos);

        verify(userGroupQueryRepository, times(1))
                .getMyGroupMemory(any(Pageable.class), anyLong(), anyLong());
    }


    @DisplayName("로그인한 사용자가 업로드한 이미지 용량을 체크한다.")
    @Test
    void checkImageStorageVolumeLoginUserUse() {

        // given
        given(awsService.calculateImageVolume(anyString(), anyString()))
                .willReturn(1L);

        // when
        Integer volume = userService.checkImageStorageVolumeLoginUserUse("jemin");

        // then
        verify(awsService, times(1))
                .calculateImageVolume(anyString(), anyString());
    }


    @DisplayName("사용자 ID로 사용자 정보를 조회할때")
    @Nested
    class When_Get_UserInfo_By_Identifier {

        @DisplayName("사용자가 존재하면 사용자 정보를 얻을 수 있다.")
        @Test
        void find_user_nickname_by_user_indentifier() {

            // given
            User emailLoginUser = UserFactory.createEmailLoginUser();
            given(userRepository.findById(anyLong()))
                    .willReturn(Optional.of(emailLoginUser));

            // when
            String nickname = userService.findNicknameByUserIdentifier(1L);

            // then
            assertThat(nickname).isEqualTo(emailLoginUser.getNickname());

            verify(userRepository, times(1)).findById(anyLong());
        }

        @DisplayName("사용자가 존재하지 않으면 예외가 발생한다.")
        @Test
        void find_user_nickname_by_user_indentifier_exception() {

            // given
            given(userRepository.findById(anyLong()))
                    .willThrow(new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_USER));

            // when
            assertThatThrownBy(() -> {
                userService.findNicknameByUserIdentifier(anyLong());
            })
                    .isInstanceOf(GlobalBadRequestException.class).hasMessage("해당 id의 유저가 없습니다.");

            // then
            verify(userRepository, times(1)).findById(anyLong());
        }
    }

    @DisplayName("사용자 프로필 수정을 위한 사용자 정보 조회")
    @Test
    void find_user_profile_data_for_update() {

        // given
        User emailLoginUser = UserFactory.createEmailLoginUser();
        given(authenticatedUserChecker.checkAuthenticatedUserExist(anyLong()))
                .willReturn(emailLoginUser);

        // when
        ProfileUpdateFormResponseDto loginUserProfileDataForUpdate = userService.getLoginUserProfileDataForUpdate(anyLong());

        // then
        assertThat(loginUserProfileDataForUpdate.getNickname()).isEqualTo(emailLoginUser.getNickname());
        assertThat(loginUserProfileDataForUpdate.getProfileImage()).isEqualTo(emailLoginUser.getProfileImage());
        assertThat(loginUserProfileDataForUpdate.getGender()).isEqualTo(emailLoginUser.getGender());
        assertThat(loginUserProfileDataForUpdate.getAgeGroup()).isEqualTo(emailLoginUser.getAgeGroup());

        verify(authenticatedUserChecker, times(1)).checkAuthenticatedUserExist(anyLong());
    }

    @DisplayName("유저 프로필을 변경할때")
    @Nested
    class When_update_user_profile {

        @DisplayName("프로필 이미지가 삭제 되면 profileImage를 null로 설정한다.")
        @Test
        void remove_profile_image() throws IOException {

            // given
            User user = UserFactory.createEmailLoginUser();

            MockMultipartFile mockMultipartFile = new MockMultipartFile("testImage", "test", "multipart/form-data", new FileInputStream("/Users/seojemin/IdeaProjects/Melly_Server/src/test/resources/image/testimage.jpg"));
            ProfileUpdateRequestDto profileImageDeleteDto = ProfileUpdateRequestDto.builder()
                    .profileImage(mockMultipartFile)
                    .userSeq(1L)
                    .deleteImage(true)
                    .gender(Gender.MALE)
                    .ageGroup(AgeGroup.TWO)
                    .nickname("jemin")
                    .build();

            given(authenticatedUserChecker.checkAuthenticatedUserExist(anyLong()))
                    .willReturn(user);
            // when
            userService.updateLoginUserProfile(profileImageDeleteDto);

            // then
            assertThat(user.getProfileImage()).isNull();

            verify(authenticatedUserChecker, times(1)).checkAuthenticatedUserExist(anyLong());
        }

        @DisplayName("새로운 프로필 이미지를 설정하면 S3 업로드 후, 프로필로 저장된다.")
        @Test
        void set_new_profile_image_after_s3_upload() throws IOException {

            // given
            User user = UserFactory.createEmailLoginUser();

            MockMultipartFile mockMultipartFile = new MockMultipartFile("testImage", "test", "multipart/form-data", new FileInputStream("/Users/seojemin/IdeaProjects/Melly_Server/src/test/resources/image/testimage.jpg"));
            ProfileUpdateRequestDto profileImageDeleteDto = ProfileUpdateRequestDto.builder()
                    .profileImage(mockMultipartFile)
                    .userSeq(1L)
                    .deleteImage(false)
                    .gender(Gender.MALE)
                    .ageGroup(AgeGroup.TWO)
                    .nickname("jemin")
                    .build();

            given(authenticatedUserChecker.checkAuthenticatedUserExist(anyLong()))
                    .willReturn(user);

            given(s3FileLoader.getMultipartFileName(any(MultipartFile.class)))
                    .willReturn("uploaded_image_name.png");

            // when
            userService.updateLoginUserProfile(profileImageDeleteDto);

            // then
            assertThat(user.getProfileImage()).isEqualTo("uploaded_image_name.png");

            verify(authenticatedUserChecker, times(1)).checkAuthenticatedUserExist(anyLong());
            verify(s3FileLoader,times(1)).getMultipartFileName(any(MultipartFile.class));
        }
    }
}