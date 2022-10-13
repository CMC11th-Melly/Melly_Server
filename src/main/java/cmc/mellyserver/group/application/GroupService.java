package cmc.mellyserver.group.application;

import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.group.domain.GroupAndUser;
import cmc.mellyserver.group.domain.GroupRepository;
import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.group.presentation.dto.GroupCreateRequest;
import cmc.mellyserver.user.domain.User;
import cmc.mellyserver.user.domain.UserRepository;
import cmc.mellyserver.user.presentation.dto.ParticipateGroupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final AuthenticatedUserChecker authenticatedUserChecker;


    @Transactional
    public UserGroup saveGroup(String uid, GroupCreateRequest groupCreateRequest)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        UserGroup userGroup = new UserGroup(groupCreateRequest.getGroupName(), "hello.co.kr", groupCreateRequest.getGroupType());
        GroupAndUser groupAndUser = new GroupAndUser();
        groupAndUser.setUser(user);
        userGroup.setGroupUser(groupAndUser);
        return groupRepository.save(userGroup);
    }

    @Transactional
    public UserGroup participateToGroup(String uid, Long groupId)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        UserGroup userGroup = groupRepository.findById(groupId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_GROUP);
        });
        GroupAndUser groupAndUser = new GroupAndUser();
        groupAndUser.setUser(user);
        userGroup.setGroupUser(groupAndUser);
        return groupRepository.save(userGroup);
    }

}
