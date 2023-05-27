package cmc.mellyserver.user.application.impl;

import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.common.util.aws.S3FileLoader;
import cmc.mellyserver.group.application.GroupService;
import cmc.mellyserver.group.domain.repository.UserGroupQueryRepository;
import cmc.mellyserver.common.enums.GroupType;
import cmc.mellyserver.memory.domain.repository.MemoryQueryRepository;
import cmc.mellyserver.memory.domain.repository.dto.MemoryResponseDto;
import cmc.mellyserver.user.application.UserService;
import cmc.mellyserver.user.application.dto.response.SurveyRecommendResponseDto;
import cmc.mellyserver.user.application.dto.response.ProfileUpdateFormResponseDto;
import cmc.mellyserver.user.domain.User;
import cmc.mellyserver.user.domain.repository.UserRepository;
import cmc.mellyserver.user.infrastructure.SurveyRecommender;
import cmc.mellyserver.user.presentation.dto.request.ProfileUpdateRequestDto;
import cmc.mellyserver.user.presentation.dto.request.SurveyRequestDto;
import cmc.mellyserver.user.application.dto.response.GroupLoginUserParticipatedResponseDto;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthenticatedUserChecker authenticatedUserChecker;

    private final MemoryQueryRepository memoryQueryRepository;

    private final GroupService groupService;

    private final AmazonS3Client amazonS3Client;

    private final S3FileLoader s3FileLoader;

    private final UserGroupQueryRepository userGroupQueryRepository;

    private final SurveyRecommender surveyRecommender;

    private final UserRepository userRepository;


    @Override
    public SurveyRecommendResponseDto getSurveyResult(Long userSeq)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(userSeq);
        return surveyRecommender.getRecommend(user);
    }


    @Override
    public List<GroupLoginUserParticipatedResponseDto> findGroupListLoginUserParticiated(Long userSeq)
    {
       return userGroupQueryRepository.getGroupListLoginUserParticipate(userSeq);
    }


    @Override
    public Slice<MemoryResponseDto> findMemoriesLoginUserWrite(Pageable pageable, Long userSeq, GroupType groupType)
    {
       return memoryQueryRepository.searchMemoryUserCreatedForMyPage(pageable, userSeq, groupType);
    }


    @Override
    public Slice<MemoryResponseDto> findMemoriesUsersBelongToMyGroupWrite(Pageable pageable,Long groupId,Long userSeq) {
        return userGroupQueryRepository.getMyGroupMemory(pageable, groupId, userSeq);
    }


    @Override
    public Integer checkImageStorageVolumeLoginUserUse(String username)
    {
        ObjectListing mellyimage = amazonS3Client.listObjects("mellyimage", username);
        List<S3ObjectSummary> objectSummaries = mellyimage.getObjectSummaries();
        Long sum = objectSummaries.stream().mapToLong(S3ObjectSummary::getSize).sum();
        return  sum.intValue();
    }


    @Override
    public ProfileUpdateFormResponseDto getLoginUserProfileDataForUpdate(Long userSeq)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(userSeq);
        return new ProfileUpdateFormResponseDto(user.getProfileImage(),user.getNickname(),user.getGender(),user.getAgeGroup());
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
    public void createSurvey(SurveyRequestDto surveyRequestDto)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(surveyRequestDto.getUserSeq());
        user.addSurveyData(surveyRequestDto.getRecommendGroup(),surveyRequestDto.getRecommendPlace(),surveyRequestDto.getRecommendActivity());
    }


    @Override
    @Transactional
    public void participateToGroup(Long userSeq, Long groupId)
    {
        groupService.participateToGroup(userSeq, groupId);
    }


    @Override
    @Transactional
    public void updateLoginUserProfile(ProfileUpdateRequestDto profileUpdateRequestDto)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(profileUpdateRequestDto.getUserSeq());

        if(profileUpdateRequestDto.isDeleteImage())
        {
            user.updateProfile(profileUpdateRequestDto.getNickname(),profileUpdateRequestDto.getGender(),profileUpdateRequestDto.getAgeGroup(), null);
        }
        else
        {
            user.updateProfile(profileUpdateRequestDto.getNickname(),profileUpdateRequestDto.getGender(),profileUpdateRequestDto.getAgeGroup(), s3FileLoader.getMultipartFileName(profileUpdateRequestDto.getProfileImage()));
        }
    }
}
