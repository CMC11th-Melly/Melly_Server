package cmc.mellyserver.mellyappexternalapi.group.application.impl;

import cmc.mellyserver.mellyappexternalapi.common.auth.AuthenticatedUserChecker;
import cmc.mellyserver.mellyappexternalapi.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.mellyappexternalapi.common.exception.GlobalBadRequestException;
import cmc.mellyserver.mellyappexternalapi.group.application.GroupService;
import cmc.mellyserver.mellyappexternalapi.group.application.dto.request.CreateGroupRequestDto;
import cmc.mellyserver.mellyappexternalapi.group.application.dto.request.UpdateGroupRequestDto;
import cmc.mellyserver.mellydomain.group.domain.GroupAndUser;
import cmc.mellyserver.mellydomain.group.domain.UserGroup;
import cmc.mellyserver.mellydomain.group.domain.repository.GroupAndUserRepository;
import cmc.mellyserver.mellydomain.group.domain.repository.GroupRepository;
import cmc.mellyserver.mellydomain.user.domain.User;
import cmc.mellyserver.mellydomain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    private final GroupAndUserRepository groupAndUserRepository;

    private final UserRepository userRepository;

    private final AuthenticatedUserChecker authenticatedUserChecker;

    @Cacheable(value = "groupInfo", key = "#groupId")
    @Override
    public UserGroup findGroupById(Long groupId) {

        return groupRepository.findById(groupId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_GROUP);
        });
    }

    @Override
    @Transactional
    public UserGroup saveGroup(CreateGroupRequestDto createGroupRequestDto) {

        UserGroup userGroup = new UserGroup(createGroupRequestDto.getGroupName(), "-", createGroupRequestDto.getGroupType(), createGroupRequestDto.getGroupIcon(), createGroupRequestDto.getUserSeq());
        userGroup.assignGroupOwner(createGroupRequestDto.getUserSeq());
        return groupRepository.save(userGroup);
    }


    @Override
    @Transactional
    public void participateToGroup(Long userSeq, Long groupId) {

        User user = userRepository.findById(userSeq).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_USER);
        });

        UserGroup userGroup = groupRepository.findById(groupId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_GROUP);
        });

        checkUserAlreadyParticipatedInGroup(user, userGroup);

        groupAndUserRepository.save(GroupAndUser.of(user, userGroup));
    }


    @Override
    @Transactional
    public void updateGroup(Long userSeq, UpdateGroupRequestDto updateGroupRequestDto) {

        UserGroup userGroup = groupRepository.findById(updateGroupRequestDto.getGroupId()).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_GROUP);
        });
        userGroup.updateUserGroup(userSeq, updateGroupRequestDto.getGroupName(), updateGroupRequestDto.getGroupType(), updateGroupRequestDto.getGroupIcon());
    }

    @Override
    @Transactional
    public void removeGroup(Long userSeq, Long groupId) {

        UserGroup userGroup = groupRepository.findById(groupId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_GROUP);
        });
        userGroup.remove(userSeq);
    }

    private void checkUserAlreadyParticipatedInGroup(User user, UserGroup userGroup) {
        if (groupAndUserRepository.findGroupAndUserByUserAndGroup(user, userGroup).isPresent()) {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.DUPLICATED_GROUP);
        }
    }
}
