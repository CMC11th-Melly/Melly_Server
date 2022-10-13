package cmc.mellyserver.user.application;

import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.group.application.GroupService;
import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final AuthenticatedUserChecker authenticatedUserChecker;
    private final GroupService groupService;

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
}
