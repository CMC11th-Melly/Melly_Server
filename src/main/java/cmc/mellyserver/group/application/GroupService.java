package cmc.mellyserver.group.application;

import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.util.AuthenticatedUserChecker;
import cmc.mellyserver.group.domain.GroupAndUser;
import cmc.mellyserver.group.domain.GroupRepository;
import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.group.presentation.dto.GroupCreateRequest;
import cmc.mellyserver.user.domain.User;
import cmc.mellyserver.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final AuthenticatedUserChecker authenticatedUserChecker;
    // group 만들때 더해야 하는 것
    // 일단 그룹 생성자와 그룹 멤버가 빠지면 안되지
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
}
