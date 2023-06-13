package cmc.mellyserver.mellyapi.user.application.impl;

import cmc.mellyserver.mellyapi.common.auth.AuthenticatedUserChecker;
import cmc.mellyserver.mellyapi.common.aws.AwsService;
import cmc.mellyserver.mellyapi.common.aws.FileUploader;
import cmc.mellyserver.mellyapi.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.mellyapi.common.exception.GlobalBadRequestException;
import cmc.mellyserver.mellyapi.group.application.GroupService;
import cmc.mellyserver.mellyapi.user.application.UserService;
import cmc.mellyserver.mellyapi.user.application.dto.request.SurveyRequestDto;
import cmc.mellyserver.mellyapi.user.application.dto.response.ProfileUpdateFormResponseDto;
import cmc.mellyserver.mellyapi.user.application.dto.response.ProfileUpdateRequestDto;
import cmc.mellyserver.mellycore.common.enums.GroupType;
import cmc.mellyserver.mellycore.group.domain.repository.UserGroupQueryRepository;
import cmc.mellyserver.mellycore.group.domain.repository.dto.GroupLoginUserParticipatedResponseDto;
import cmc.mellyserver.mellycore.memory.domain.repository.MemoryQueryRepository;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.MemoryResponseDto;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.repository.UserRepository;
import cmc.mellyserver.mellycore.user.infrastructure.SurveyRecommendResponseDto;
import cmc.mellyserver.mellycore.user.infrastructure.SurveyRecommender;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthenticatedUserChecker authenticatedUserChecker;

    private final MemoryQueryRepository memoryQueryRepository;

    private final GroupService groupService;

    private final AwsService awsService;

    private final FileUploader fileUploader;

    private final RedisTemplate redisTemplate;

    private final UserGroupQueryRepository userGroupQueryRepository;

    private final SurveyRecommender surveyRecommender;

    private final UserRepository userRepository;

    @Override
    public SurveyRecommendResponseDto getSurveyResult(Long userSeq) {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(userSeq);
        return surveyRecommender.getRecommend(user.getRecommend().getRecommendGroup());
    }

    @Cacheable(value = "groupList", key = "#userSeq", cacheManager = "redisCacheManager")
    @Override
    public List<GroupLoginUserParticipatedResponseDto> findGroupListLoginUserParticiated(
            Long userSeq) {
        return userGroupQueryRepository.getGroupListLoginUserParticipate(userSeq);
    }

    @Override
    public Slice<MemoryResponseDto> findMemoriesLoginUserWrite(Pageable pageable, Long userSeq,
            GroupType groupType) {
        return memoryQueryRepository.searchMemoryUserCreatedForMyPage(pageable, userSeq, groupType);
    }

    @Override
    public Slice<MemoryResponseDto> findMemoriesUsersBelongToMyGroupWrite(Pageable pageable,
            Long groupId,
            Long userSeq) {
        return userGroupQueryRepository.getMyGroupMemory(pageable, groupId, userSeq);
    }

    @Override
    public Integer checkImageStorageVolumeLoginUserUse(String username) {
        return awsService.calculateImageVolume("mellyimage", username).intValue();
    }

    @Override
    public ProfileUpdateFormResponseDto getLoginUserProfileDataForUpdate(Long userSeq) {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(userSeq);
        return new ProfileUpdateFormResponseDto(user.getProfileImage(), user.getNickname(),
                user.getGender(),
                user.getAgeGroup());
    }

    @Override
    public String findNicknameByUserIdentifier(Long userSeq) {

        User user = userRepository.findById(userSeq).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_USER);
        });
        return user.getNickname();
    }

    @Override
    @Transactional
    public void createSurvey(SurveyRequestDto surveyRequestDto) {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(
                surveyRequestDto.getUserSeq());
        user.addSurveyData(surveyRequestDto.getRecommendGroup(),
                surveyRequestDto.getRecommendPlace(),
                surveyRequestDto.getRecommendActivity());
    }

    @Override
    @Transactional
    public void participateToGroup(Long userSeq, Long groupId) {
        groupService.participateToGroup(userSeq, groupId);
    }

    @Override
    @Transactional
    public void updateLoginUserProfile(ProfileUpdateRequestDto profileUpdateRequestDto) {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(
                profileUpdateRequestDto.getUserSeq());
        user.updateProfile(profileUpdateRequestDto.getNickname(),
                profileUpdateRequestDto.getGender(),
                profileUpdateRequestDto.getAgeGroup());
        if (profileUpdateRequestDto.isDeleteImage()) {
            user.chnageProfileImage(null);
        } else {
            user.chnageProfileImage(
                    fileUploader.getMultipartFileName(profileUpdateRequestDto.getProfileImage()));
        }
    }
}
