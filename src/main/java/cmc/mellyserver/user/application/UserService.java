package cmc.mellyserver.user.application;

import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.common.util.aws.S3FileLoader;
import cmc.mellyserver.group.application.GroupService;
import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.group.domain.UserGroupQueryRepository;
import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.domain.MemoryQueryRepository;
import cmc.mellyserver.memory.presentation.dto.GetUserMemoryCond;
import cmc.mellyserver.memory.presentation.dto.ImageDto;
import cmc.mellyserver.user.application.dto.GroupMemory;
import cmc.mellyserver.user.application.dto.ProfileUpdateFormResponse;
import cmc.mellyserver.user.domain.User;
import cmc.mellyserver.user.presentation.dto.ProfileUpdateRequest;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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


    public List<UserGroup> getUserGroup(String uid) {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        return user.getGroupAndUsers().stream().map(gu -> gu.getGroup()).collect(Collectors.toList());
    }

    public Slice<Memory> getUserMemory(Pageable pageable, String uid, GroupType groupType) {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);

        return memoryQueryRepository.searchMemoryUserCreate(pageable, user.getUserSeq(),
                null, groupType);

    }

    @Transactional
    public void participateToGroup(String uid, Long groupId) {
        groupService.participateToGroup(uid, groupId);
    }

    public int checkUserImageVolume(String username) {

        ObjectListing mellyimage = amazonS3Client.listObjects("mellyimage", username);
        List<S3ObjectSummary> objectSummaries = mellyimage.getObjectSummaries();
        Long sum = objectSummaries.stream().mapToLong(S3ObjectSummary::getSize).sum();
        return  sum.intValue();
    }

    @Transactional
    public void updateProfile(String uid, ProfileUpdateRequest profileUpdateRequest) {

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        String multipartFileName = s3FileLoader.getMultipartFileName(profileUpdateRequest.getProfileImage());
        user.updateProfile(profileUpdateRequest.getNickname(),profileUpdateRequest.getGender(),profileUpdateRequest.getAgeGroup(), multipartFileName);
    }


    public ProfileUpdateFormResponse getProfileDataForUpdate(String uid) {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        return new ProfileUpdateFormResponse(user.getProfileImage(),user.getNickname(),user.getGender(),user.getAgeGroup());
    }


    // 내 그룹사람들이 이 그룹으로 쓴 메모리
    public Slice<GroupMemory> getMemoryBelongToMyGroup(Pageable pageable, Long groupId,Long userSeq) {

        Slice<Memory> myGroupMemory = userGroupQueryRepository.getMyGroupMemory(pageable, groupId,userSeq);
        return myGroupMemory.map(m -> new GroupMemory(m.getPlace().getId(),
                m.getPlace().getPlaceName(),
                m.getId(),
                m.getMemoryImages().stream().map(mi -> new ImageDto(mi.getId(),mi.getImagePath())).collect(Collectors.toList()),
                m.getTitle(),
                m.getContent(),
                m.getGroupInfo().getGroupType(),
                m.getGroupInfo().getGroupName(),
                m.getStars(),
                m.getKeyword(),
                m.getVisitedDate()));

    }
}
