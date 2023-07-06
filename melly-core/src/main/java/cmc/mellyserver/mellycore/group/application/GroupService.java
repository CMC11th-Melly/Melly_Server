package cmc.mellyserver.mellycore.group.application;

import cmc.mellyserver.mellycommon.codes.ErrorCode;
import cmc.mellyserver.mellycore.common.exception.GlobalBadRequestException;
import cmc.mellyserver.mellycore.group.application.dto.request.CreateGroupRequestDto;
import cmc.mellyserver.mellycore.group.application.dto.request.UpdateGroupRequestDto;
import cmc.mellyserver.mellycore.group.domain.GroupAndUser;
import cmc.mellyserver.mellycore.group.domain.UserGroup;
import cmc.mellyserver.mellycore.group.domain.repository.GroupAndUserRepository;
import cmc.mellyserver.mellycore.group.domain.repository.GroupRepository;
import cmc.mellyserver.mellycore.group.domain.repository.UserGroupQueryRepository;
import cmc.mellyserver.mellycore.group.domain.repository.dto.GroupListForSaveMemoryResponseDto;
import cmc.mellyserver.mellycore.group.domain.repository.dto.GroupLoginUserParticipatedResponseDto;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    private final GroupAndUserRepository groupAndUserRepository;

    private final UserRepository userRepository;

    private final UserGroupQueryRepository userGroupQueryRepository;

    @Transactional(readOnly = true)
    public UserGroup findGroupById(Long groupId) {

        return groupRepository.findById(groupId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ErrorCode.NO_SUCH_GROUP);
        });
    }

    //    @Cacheable(value = "groupList", key = "#userSeq", cacheManager = "redisCacheManager")
    @Transactional(readOnly = true)
    public List<GroupLoginUserParticipatedResponseDto> findGroupListLoginUserParticiated(Long userSeq) throws InterruptedException {
        Thread.sleep(3000);
        return userGroupQueryRepository.getGroupListLoginUserParticipate(userSeq);
    }

    @Cacheable(value = "findGroups", key = "#userSeq")
    public List<GroupListForSaveMemoryResponseDto> findGroupListLoginUserParticipateForMemoryCreate(Long userSeq) {
        return userGroupQueryRepository.getUserGroupListForMemoryEnroll(userSeq);
    }


    @Transactional
    public UserGroup saveGroup(CreateGroupRequestDto createGroupRequestDto) {

        UserGroup userGroup = createGroupRequestDto.toEntity();
        userGroup.assignGroupManager(createGroupRequestDto.getUserSeq());
        return groupRepository.save(userGroup);
    }


    @Transactional
    public void participateToGroup(Long userSeq, Long groupId) {

        User user = userRepository.findById(userSeq).orElseThrow(() -> {
            throw new GlobalBadRequestException(ErrorCode.NO_SUCH_USER);
        });

        UserGroup userGroup = groupRepository.findById(groupId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ErrorCode.NO_SUCH_GROUP);
        });

        checkUserAlreadyParticipatedInGroup(user, userGroup);

        groupAndUserRepository.save(GroupAndUser.of(user, userGroup));
    }


    @Transactional
    public void updateGroup(Long userSeq, UpdateGroupRequestDto updateGroupRequestDto) {

        UserGroup userGroup = groupRepository.findById(updateGroupRequestDto.getGroupId()).orElseThrow(() -> {
            throw new GlobalBadRequestException(ErrorCode.NO_SUCH_GROUP);
        });
        userGroup.update(userSeq, updateGroupRequestDto.getGroupName(), updateGroupRequestDto.getGroupType(), updateGroupRequestDto.getGroupIcon());
    }


    @Transactional
    public void removeGroup(Long userSeq, Long groupId) {

        UserGroup userGroup = groupRepository.findById(groupId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ErrorCode.NO_SUCH_GROUP);
        });
        userGroup.remove(userSeq);
    }

    private void checkUserAlreadyParticipatedInGroup(User user, UserGroup userGroup) {
        if (groupAndUserRepository.findGroupAndUserByUserAndGroup(user, userGroup).isPresent()) {
            throw new GlobalBadRequestException(ErrorCode.DUPLICATED_GROUP);
        }
    }
}
