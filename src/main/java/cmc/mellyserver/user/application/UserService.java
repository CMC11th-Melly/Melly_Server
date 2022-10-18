package cmc.mellyserver.user.application;

import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.common.util.aws.S3FileLoader;
import cmc.mellyserver.group.application.GroupService;
import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.domain.MemoryQueryRepository;
import cmc.mellyserver.memory.presentation.dto.GetUserMemoryCond;
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


    public List<UserGroup> getUserGroup(String uid) {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        return user.getGroupAndUsers().stream().map(gu -> gu.getGroup()).collect(Collectors.toList());
    }

    public Slice<Memory> getUserMemory(Long lastMemoryId, Pageable pageable, String uid, GetUserMemoryCond getUserMemoryCond) {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);

        return memoryQueryRepository.searchMemoryUserCreate(lastMemoryId, pageable, user.getUserSeq(),
                null,
                getUserMemoryCond.getKeyword(),
                getUserMemoryCond.getGroupType(),
                getUserMemoryCond.getVisitedDate());


    }

    @Transactional
    public void participateToGroup(String uid, Long groupId) {
        groupService.participateToGroup(uid, groupId);
    }

    public double checkUserImageVolume(String username) {

        ObjectListing mellyimage = amazonS3Client.listObjects("mellyimage", username);
        List<S3ObjectSummary> objectSummaries = mellyimage.getObjectSummaries();
        double sum = (double) objectSummaries.stream().mapToLong(S3ObjectSummary::getSize).sum();
        return Math.round(((sum / 1000.0) / 1024.0) * 100) / 100.0;
    }

    @Transactional
    public void updateProfile(String uid, ProfileUpdateRequest profileUpdateRequest) {

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        String multipartFileName = s3FileLoader.getMultipartFileName(profileUpdateRequest.getImage());
        user.updateProfile(profileUpdateRequest.getNickname(), multipartFileName);
    }
}
