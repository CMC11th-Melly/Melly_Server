package cmc.mellyserver.user.application;

import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.common.util.aws.S3FileLoader;
import cmc.mellyserver.group.application.GroupService;
import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.group.domain.UserGroupQueryRepository;
import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.domain.MemoryQueryRepository;
import cmc.mellyserver.memory.presentation.dto.common.ImageDto;
import cmc.mellyserver.user.application.dto.GroupMemory;
import cmc.mellyserver.user.application.dto.PollRecommendResponse;
import cmc.mellyserver.user.application.dto.ProfileUpdateFormResponse;
import cmc.mellyserver.user.domain.User;
import cmc.mellyserver.user.domain.UserRepository;
import cmc.mellyserver.user.presentation.dto.NotificationOnOffResponse;
import cmc.mellyserver.user.presentation.dto.common.UserAssembler;
import cmc.mellyserver.user.presentation.dto.request.SurveyRequest;
import cmc.mellyserver.user.presentation.dto.request.ProfileUpdateRequest;
import cmc.mellyserver.user.presentation.dto.response.GetUserMemoryResponse;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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



    public PollRecommendResponse getSurvey(String uid)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        return surveyRecommender.getRecommend(user);
    }


    public List<UserGroup> getUserGroup(String uid)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        return user.getGroupAndUsers().stream().map(gu -> gu.getGroup()).collect(Collectors.toList());
    }


    public Slice<GetUserMemoryResponse> getUserMemory(Pageable pageable, String uid, GroupType groupType)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        Slice<Memory> memories = memoryQueryRepository.searchMemoryUserCreate(pageable, uid, null, groupType);
        return UserAssembler.getUserMemoryResponses(memories, user);
    }


    public int checkUserImageVolume(String username)
    {
        ObjectListing mellyimage = amazonS3Client.listObjects("mellyimage", username);
        List<S3ObjectSummary> objectSummaries = mellyimage.getObjectSummaries();
        Long sum = objectSummaries.stream().mapToLong(S3ObjectSummary::getSize).sum();
        return  sum.intValue();
    }


    public ProfileUpdateFormResponse getProfileDataForUpdate(String uid)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        return new ProfileUpdateFormResponse(user.getProfileImage(),user.getNickname(),user.getGender(),user.getAgeGroup());
    }


    public Slice<GroupMemory> getMemoryBelongToMyGroup(Pageable pageable,Long groupId,String uid,Long userSeq) {

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        Slice<Memory> myGroupMemory = userGroupQueryRepository.getMyGroupMemory(pageable, groupId,user,userSeq);
        return myGroupMemory.map(m -> new GroupMemory(m.getPlace().getId(),
                m.getPlace().getPlaceName(),
                m.getId(),
                m.getMemoryImages().stream().map(mi -> new ImageDto(mi.getId(),mi.getImagePath())).collect(Collectors.toList()),
                m.getTitle(),
                m.getContent(),
                m.getGroupInfo().getGroupType(),
                m.getGroupInfo().getGroupName(),
                m.getStars(),
                user.getMemories().stream().anyMatch((um -> um.getId().equals(m.getId()))),
                m.getKeyword(),
                m.getVisitedDate()));

    }


    @Transactional
    public void createSurvey(String uid, SurveyRequest pollRequest)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);

        user.addPollData(pollRequest.getRecommendGroup(),pollRequest.getRecommendPlace(),pollRequest.getRecommendActivity());
    }


    @Transactional
    public void participateToGroup(String uid, Long groupId)
    {
        groupService.participateToGroup(uid, groupId);
    }


    @Transactional
    public void updateProfile(String uid, ProfileUpdateRequest profileUpdateRequest)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        if(profileUpdateRequest.isDeleteImage() == true)
        {
            user.setProfileImage(null);
            user.updateProfile(profileUpdateRequest.getNickname(),profileUpdateRequest.getGender(),profileUpdateRequest.getAgeGroup(), null);
        }
        else
        {
            String multipartFileName = s3FileLoader.getMultipartFileName(profileUpdateRequest.getProfileImage());
            user.updateProfile(profileUpdateRequest.getNickname(),profileUpdateRequest.getGender(),profileUpdateRequest.getAgeGroup(), multipartFileName);
        }

    }


    public String getUserNickname(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_USER);
        });
        return user.getNickname();

    }
}
