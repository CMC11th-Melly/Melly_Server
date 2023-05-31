package cmc.mellyserver.group.application.impl;

import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.group.application.GroupService;
import cmc.mellyserver.group.application.dto.request.CreateGroupRequestDto;
import cmc.mellyserver.group.application.dto.request.UpdateGroupRequestDto;
import cmc.mellyserver.group.domain.GroupAndUser;
import cmc.mellyserver.group.domain.repository.GroupAndUserRepository;
import cmc.mellyserver.group.domain.repository.GroupRepository;
import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    private final GroupAndUserRepository groupAndUserRepository;

    private final AuthenticatedUserChecker authenticatedUserChecker;


    @Override
    public UserGroup findGroupById(Long groupId)
    {
      return groupRepository.findById(groupId).orElseThrow(()-> {throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_GROUP);});
    }


    @Override
    @Transactional
    public UserGroup saveGroup(CreateGroupRequestDto createGroupRequestDto)
    {
        UserGroup userGroup = new UserGroup(createGroupRequestDto.getGroupName(), "-", createGroupRequestDto.getGroupType(), createGroupRequestDto.getGroupIcon(), createGroupRequestDto.getUserSeq());
        userGroup.setCreatorId(createGroupRequestDto.getUserSeq());
        return groupRepository.save(userGroup);
    }


    @Override
    @Transactional
    public void participateToGroup(Long userSeq, Long groupId)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(userSeq);

        UserGroup userGroup = groupRepository.findById(groupId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_GROUP);
        });

        if(groupAndUserRepository.findGroupAndUserByUserAndGroup(user, userGroup).isPresent()) throw new GlobalBadRequestException(ExceptionCodeAndDetails.DUPLICATED_GROUP);
        groupAndUserRepository.save(new GroupAndUser(user, userGroup));
    }


    @Override
    @Transactional
    public void updateGroup(UpdateGroupRequestDto updateGroupRequestDto) {

        UserGroup userGroup = groupRepository.findById(updateGroupRequestDto.getGroupId()).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_GROUP);
        });
        userGroup.updateUserGroup(updateGroupRequestDto.getGroupName(),updateGroupRequestDto.getGroupType(),updateGroupRequestDto.getGroupIcon());
    }


    @Override
    @Transactional
    public void removeGroup(Long userSeq, Long groupId)
    {
        UserGroup userGroup = groupRepository.findById(groupId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_GROUP);
        });

        if (!hasPermissionToRemove(userGroup,userSeq))
        {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_AUTHORITY_TO_REMOVE);
        }

        userGroup.remove();
    }

    private boolean hasPermissionToRemove(UserGroup userGroup,Long userSeq)
    {
        return userGroup.getCreatorId().equals(userSeq);
    }
}