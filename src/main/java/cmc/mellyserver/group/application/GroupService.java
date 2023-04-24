package cmc.mellyserver.group.application;

import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.group.domain.GroupAndUser;
import cmc.mellyserver.group.domain.group.GroupAndUserRepository;
import cmc.mellyserver.group.domain.group.GroupRepository;
import cmc.mellyserver.group.domain.group.UserGroup;
import cmc.mellyserver.group.presentation.dto.request.GroupCreateRequest;
import cmc.mellyserver.group.presentation.dto.request.GroupUpdateRequest;
import cmc.mellyserver.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupAndUserRepository groupAndUserRepository;
    private final AuthenticatedUserChecker authenticatedUserChecker;


    // TODO : 수정 완료 (2023.04.16)
    public UserGroup getGroupById(Long groupId)
    {
      return groupRepository.findById(groupId).orElseThrow(()-> {throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_GROUP);});
    }


    // TODO : 수정 완료 (2023.04.16)
    @Transactional
    public UserGroup saveGroup(Long userSeq, GroupCreateRequest groupCreateRequest)
    {
        UserGroup userGroup = new UserGroup(groupCreateRequest.getGroupName(), "-", groupCreateRequest.getGroupType(), groupCreateRequest.getGroupIcon(), userSeq);
        userGroup.setCreatorId(userSeq);
        return groupRepository.save(userGroup);
    }



    // TODO : 다시 안봐
    @Transactional
    public void participateToGroup(Long userSeq, Long groupId)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(userSeq);

        UserGroup userGroup = groupRepository.findById(groupId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_GROUP);
        });

        Optional<GroupAndUser> groupAndUserByUserAndGroup = groupAndUserRepository.findGroupAndUserByUserAndGroup(user, userGroup);

        if(groupAndUserByUserAndGroup.isPresent()) throw new GlobalBadRequestException(ExceptionCodeAndDetails.DUPLICATED_GROUP);

        GroupAndUser groupAndUser = new GroupAndUser(user, userGroup);
        groupAndUserRepository.save(groupAndUser);
    }


    // TODO : 수정 완료 (2023.04.16)
    @Transactional
    public void updateGroup(Long groupId, GroupUpdateRequest groupUpdateRequestDto) {

        UserGroup userGroup = groupRepository.findById(groupId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_GROUP);
        });
        userGroup.updateUserGroup(groupUpdateRequestDto.getGroupName(),groupUpdateRequestDto.getGroupType(),groupUpdateRequestDto.getGroupIcon());

    }


    // TODO : 수정 완료
    @Transactional
    public String deleteGroup(Long userSeq, Long groupId)
    {
        UserGroup userGroup = groupRepository.findById(groupId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_GROUP);
        });

        // 만약 해당 그룹을 만든 사람이라면 삭제 가능
        if (userGroup.getCreatorId().equals(userSeq))
        {
            userGroup.remove();
            return "그룹 삭제 완료";
        }
        return "삭제 권한이 없습니다";
    }

}
