package cmc.mellyserver.user.application;

import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.group.application.GroupService;
import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.user.domain.User;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.RequiredArgsConstructor;
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
    private final GroupService groupService;
    private final AmazonS3Client amazonS3Client;
    /**
     * TODO : 유저가 속해있는 그룹
     * TODO : 유저가 작성한 메모리, 그룹 + 전체공개
     */

    public List<UserGroup> getUserGroup(String uid)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        return user.getGroupAndUsers().stream().map(gu -> gu.getGroup()).collect(Collectors.toList());
    }

    public List<Memory> getUserMemory(String uid)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        return user.getMemories();
    }

    @Transactional
    public void participateToGroup(String uid,Long groupId) {
        groupService.participateToGroup(uid,groupId);
    }

    public double checkUserImageVolume(String username) {

        System.out.println(username);
        ObjectListing mellyimage = amazonS3Client.listObjects("mellyimage", username);
        List<S3ObjectSummary> objectSummaries = mellyimage.getObjectSummaries();
        double sum = (double) objectSummaries.stream().mapToLong(S3ObjectSummary::getSize).sum();
        return Math.round(((sum/1000.0)/1024.0) * 100) / 100.0;
    }
}
