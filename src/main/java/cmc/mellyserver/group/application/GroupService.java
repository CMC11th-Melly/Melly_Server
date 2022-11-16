package cmc.mellyserver.group.application;

import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.group.domain.GroupAndUser;
import cmc.mellyserver.group.domain.GroupRepository;
import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.group.domain.UserGroupQueryRepository;
import cmc.mellyserver.group.presentation.dto.GroupCreateRequest;
import cmc.mellyserver.group.presentation.dto.GroupUpdateRequest;
import cmc.mellyserver.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final AuthenticatedUserChecker authenticatedUserChecker;


    public UserGroup getGroupById(Long groupId)
    {
      return groupRepository.findById(groupId).orElseThrow(()-> {throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_GROUP);});
    }

    @Transactional
    public UserGroup saveGroup(String uid, GroupCreateRequest groupCreateRequest)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        UserGroup userGroup = new UserGroup(groupCreateRequest.getGroupName(), "hello.co.kr", groupCreateRequest.getGroupType(),groupCreateRequest.getGroupIcon(),user.getUserSeq());
        GroupAndUser groupAndUser = new GroupAndUser();
        groupAndUser.setUser(user);
        userGroup.setGroupUser(groupAndUser);
        return groupRepository.save(userGroup);
    }

    @Transactional
    public UserGroup participateToGroup(String uid, Long groupId)
    {

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);

        boolean userParticiatedToGroup = user.getGroupAndUsers().stream().anyMatch(g -> g.getGroup().getId().equals(groupId));

        if (userParticiatedToGroup)
        {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.DUPLICATED_GROUP);
        }

        UserGroup userGroup = groupRepository.findById(groupId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_GROUP);
        });
        GroupAndUser groupAndUser = new GroupAndUser();
        groupAndUser.setUser(user);
        userGroup.setGroupUser(groupAndUser);
        return groupRepository.save(userGroup);
    }

    @Transactional
    public void updateGroup(Long groupId, GroupUpdateRequest groupUpdateRequest) {

        UserGroup userGroup = groupRepository.findById(groupId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_GROUP);
        });
        userGroup.updateUserGroup(groupUpdateRequest.getGroupName(),groupUpdateRequest.getGroupType(),groupUpdateRequest.getGroupIcon());

    }

    @Transactional
    public String deleteGroup(String uid, Long groupId)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        UserGroup userGroup = groupRepository.findById(groupId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_GROUP);
        });

//        if (userGroup.getCreatorId().equals(user.getUserSeq()))
//        {
//            userGroup.getGroupAndUsers().stream().forEach(ga -> ga.getUser().getMemories().stream().forEach(m -> {
//                if(m.getGroupInfo().getGroupId().equals(groupId))
//                {
//                    m.deleteGroupInfo();
//                }
//            }));
//            groupRepository.delete(userGroup);
//            return "그룹 삭제 완료";
//        }
//        return "삭제 권한이 없습니다";

        if(userGroup.getGroupAndUsers().size() > 1)
        {
            return "그룹원이 2명 이상이면 삭제할 수 없습니다.";
        }

        userGroup.getGroupAndUsers().stream().forEach(ga -> ga.getUser().getMemories().stream().forEach(m -> {
            if(m.getGroupInfo().getGroupId().equals(groupId))
                {
                    m.deleteGroupInfo();
                }
            }));
            groupRepository.delete(userGroup);
            return "그룹 삭제 완료";
    }

    @Transactional
    public void exitGroup(String uid, Long groupId) {

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        UserGroup userGroup = groupRepository.findById(groupId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_GROUP);
        });


        List<Long> deleteList = new ArrayList<>();

        userGroup.getGroupAndUsers().stream().forEach(ga -> {

            // 만약 이 유저 그룹에서 지금 로그인 유저를 분리하고 싶다면?
            if(ga.getUser().getUserId().equals(user.getUserId()))
            {
                ga.getUser().getMemories().stream().forEach(m -> {
                    if (m.getGroupInfo().getGroupId().equals(userGroup.getId()))
                    {
                        m.deleteGroupInfo();
                        deleteList.add(ga.getId());
                    }
                });
            }

        });

        userGroup.getGroupAndUsers().removeIf(groupAndUser -> deleteList.contains(groupAndUser.getId()));
    }
}
