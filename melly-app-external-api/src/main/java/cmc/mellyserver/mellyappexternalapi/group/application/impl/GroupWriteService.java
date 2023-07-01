package cmc.mellyserver.mellyappexternalapi.group.application.impl;

import cmc.mellyserver.mellyappexternalapi.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.mellyappexternalapi.common.exception.GlobalBadRequestException;
import cmc.mellyserver.mellyappexternalapi.group.application.dto.request.CreateGroupRequestDto;
import cmc.mellyserver.mellyappexternalapi.group.application.dto.request.UpdateGroupRequestDto;
import cmc.mellyserver.mellydomain.group.domain.GroupAndUser;
import cmc.mellyserver.mellydomain.group.domain.UserGroup;
import cmc.mellyserver.mellydomain.group.domain.repository.GroupAndUserRepository;
import cmc.mellyserver.mellydomain.group.domain.repository.GroupRepository;
import cmc.mellyserver.mellydomain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupWriteService {

    private final GroupRepository groupRepository;

    private final GroupAndUserRepository groupAndUserRepository;

    @Transactional
    public UserGroup saveGroup(CreateGroupRequestDto createGroupRequestDto) {

        UserGroup userGroup = new UserGroup(createGroupRequestDto.getGroupName(), "-", createGroupRequestDto.getGroupType(), createGroupRequestDto.getGroupIcon(), createGroupRequestDto.getUserSeq());
        userGroup.assignGroupOwner(createGroupRequestDto.getUserSeq());
        return groupRepository.save(userGroup);
    }

    @Transactional
    public void updateGroup(Long userSeq, UpdateGroupRequestDto updateGroupRequestDto) {

        UserGroup userGroup = groupRepository.findById(updateGroupRequestDto.getGroupId()).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_GROUP);
        });
        userGroup.updateUserGroup(userSeq, updateGroupRequestDto.getGroupName(), updateGroupRequestDto.getGroupType(), updateGroupRequestDto.getGroupIcon());
    }

    @Transactional
    public void removeGroup(Long userSeq, Long groupId) {

        UserGroup userGroup = groupRepository.findById(groupId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_GROUP);
        });
        userGroup.remove(userSeq);
    }

    @Transactional
    public void participateToGroup(User user, Long groupId) {

        UserGroup userGroup = groupRepository.findById(groupId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_GROUP);
        });

        checkUserAlreadyParticipatedInGroup(user, userGroup);

        groupAndUserRepository.save(GroupAndUser.of(user, userGroup));
    }

    private void checkUserAlreadyParticipatedInGroup(User user, UserGroup userGroup) {
        if (groupAndUserRepository.findGroupAndUserByUserAndGroup(user, userGroup).isPresent()) {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.DUPLICATED_GROUP);
        }
    }
}
