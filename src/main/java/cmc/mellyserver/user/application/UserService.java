package cmc.mellyserver.user.application;

import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.common.util.aws.S3FileLoader;
import cmc.mellyserver.group.application.GroupService;
import cmc.mellyserver.group.application.dto.MyGroupMemoryResponseDto;
import cmc.mellyserver.group.domain.repository.UserGroupQueryRepository;
import cmc.mellyserver.common.enums.GroupType;
import cmc.mellyserver.memory.domain.repository.MemoryQueryRepository;
import cmc.mellyserver.memory.domain.dto.MemoryResponseDto;
import cmc.mellyserver.user.application.dto.PollRecommendResponse;
import cmc.mellyserver.user.application.dto.ProfileUpdateFormResponse;
import cmc.mellyserver.user.domain.User;
import cmc.mellyserver.user.domain.repository.UserRepository;
import cmc.mellyserver.user.infrastructure.SurveyRecommender;
import cmc.mellyserver.user.presentation.dto.request.SurveyRequest;
import cmc.mellyserver.user.presentation.dto.request.ProfileUpdateRequest;
import cmc.mellyserver.user.presentation.dto.response.GetUserGroupResponseDto;
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
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final AuthenticatedUserChecker authenticatedUserChecker;
    private final MemoryQueryRepository memoryQueryRepository;
    private final GroupService groupService;
    private final AmazonS3Client amazonS3Client;
    private final S3FileLoader s3FileLoader;
    private final UserGroupQueryRepository userGroupQueryRepository;
    private final SurveyRecommender surveyRecommender;
    private final UserRepository userRepository;



    public PollRecommendResponse getSurvey(Long userSeq)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(userSeq);
        return surveyRecommender.getRecommend(user);
    }



    public List<GetUserGroupResponseDto> getGroupListLoginUserParticipate(Long userSeq)
    {
       return userGroupQueryRepository.getGroupListLoginUserParticipate(userSeq);
    }



    public Slice<MemoryResponseDto> getUserMemory(Pageable pageable, Long userSeq, GroupType groupType)
    {
       return memoryQueryRepository.searchMemoryUserCreatedForMyPage(pageable, userSeq, groupType);
    }



    public Slice<MyGroupMemoryResponseDto> getMemoryBelongToMyGroup(Pageable pageable,Long groupId,Long userSeq) {
        return userGroupQueryRepository.getMyGroupMemory(pageable, groupId, userSeq);
    }


    public int checkUserImageVolume(String username)
    {
        ObjectListing mellyimage = amazonS3Client.listObjects("mellyimage", username);
        List<S3ObjectSummary> objectSummaries = mellyimage.getObjectSummaries();
        Long sum = objectSummaries.stream().mapToLong(S3ObjectSummary::getSize).sum();
        return  sum.intValue();
    }



    public ProfileUpdateFormResponse getProfileDataForUpdate(Long userSeq)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(userSeq);
        return new ProfileUpdateFormResponse(user.getProfileImage(),user.getNickname(),user.getGender(),user.getAgeGroup());
    }




    @Transactional
    public void createSurvey(Long userSeq, SurveyRequest surveyRequest)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(userSeq);
        user.addSurveyData(surveyRequest.getRecommendGroup(),surveyRequest.getRecommendPlace(),surveyRequest.getRecommendActivity());
    }



    @Transactional
    public void participateToGroup(Long userSeq, Long groupId)
    {
        groupService.participateToGroup(userSeq, groupId);
    }



    @Transactional
    public void updateProfile(Long userSeq, ProfileUpdateRequest profileUpdateRequest)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(userSeq);

        if(profileUpdateRequest.isDeleteImage())
        {
            user.updateProfile(profileUpdateRequest.getNickname(),profileUpdateRequest.getGender(),profileUpdateRequest.getAgeGroup(), null);
        }
        else
        {
            user.updateProfile(profileUpdateRequest.getNickname(),profileUpdateRequest.getGender(),profileUpdateRequest.getAgeGroup(), s3FileLoader.getMultipartFileName(profileUpdateRequest.getProfileImage()));
        }


    }



    public String getUserNickname(Long userSeq) {

        User user = userRepository.findById(userSeq).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_USER);
        });
        return user.getNickname();
    }
}
