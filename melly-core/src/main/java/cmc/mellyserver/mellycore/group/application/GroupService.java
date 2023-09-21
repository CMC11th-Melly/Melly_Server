package cmc.mellyserver.mellycore.group.application;


import cmc.mellyserver.mellycore.common.aop.lock.annotation.DistributedLock;
import cmc.mellyserver.mellycore.common.aop.lock.annotation.OptimisticLock;
import cmc.mellyserver.mellycore.common.exception.BusinessException;
import cmc.mellyserver.mellycore.common.exception.ErrorCode;
import cmc.mellyserver.mellycore.group.application.dto.request.CreateGroupRequestDto;
import cmc.mellyserver.mellycore.group.application.dto.request.UpdateGroupRequestDto;
import cmc.mellyserver.mellycore.group.application.dto.response.GroupListLoginUserParticipatedResponse;
import cmc.mellyserver.mellycore.group.domain.GroupAndUser;
import cmc.mellyserver.mellycore.group.domain.UserGroup;
import cmc.mellyserver.mellycore.group.domain.repository.GroupAndUserRepository;
import cmc.mellyserver.mellycore.group.domain.repository.GroupRepository;
import cmc.mellyserver.mellycore.group.domain.repository.UserGroupQueryRepository;
import cmc.mellyserver.mellycore.group.domain.repository.dto.GroupDetailResponseDto;
import cmc.mellyserver.mellycore.group.domain.repository.dto.GroupLoginUserParticipatedResponseDto;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupService {

    private static final int GROUP_MEMBER_MAX_COUNT = 9;

    private final GroupRepository groupRepository;

    private final GroupAndUserRepository groupAndUserRepository;

    private final UserRepository userRepository;

    private final UserGroupQueryRepository userGroupQueryRepository;


    @Cacheable(value = "group:group-id", key = "#groupId")
    @Transactional(readOnly = true)
    public GroupDetailResponseDto getGroupDetail(final Long groupId) {

        UserGroup userGroup = groupRepository.getById(groupId);
        List<User> usersParticipatedInGroup = groupAndUserRepository.getUsersParticipatedInGroup(groupId);
        return GroupDetailResponseDto.of(userGroup, usersParticipatedInGroup);
    }


    @Transactional(readOnly = true)
    public GroupListLoginUserParticipatedResponse findGroupListLoginUserParticiated(final Long userId, final Long groupId, final Pageable pageable) {

        Slice<GroupLoginUserParticipatedResponseDto> groupListLoginUserParticipate = userGroupQueryRepository.getGroupListLoginUserParticipate(userId, groupId, pageable);

        List<GroupLoginUserParticipatedResponseDto> contents = groupListLoginUserParticipate.getContent();
        boolean next = groupListLoginUserParticipate.hasNext();
        return GroupListLoginUserParticipatedResponse.from(contents, next);
    }


    @Transactional
    public Long saveGroup(final CreateGroupRequestDto createGroupRequestDto) {

        User user = userRepository.getById(createGroupRequestDto.getCreatorId());
        UserGroup savedGroup = groupRepository.save(createGroupRequestDto.toEntity());
        groupAndUserRepository.save(GroupAndUser.of(user, savedGroup));
        return savedGroup.getId();
    }

    // TODO : 모든 어노테이션이 AOP를 기반으로 동작한다. 각각의 AOP 동작 순서 파악하기
    @CachePut(value = "group:group-id", key = "#groupId")
    @DistributedLock(key = "#groupId", waitTime = 10L, leaseTime = 2L)
    @OptimisticLock
    @Transactional
    public void participateToGroup(final Long userId, final Long groupId) {

        User user = userRepository.getById(userId);
        UserGroup userGroup = groupRepository.getById(groupId);

        validateNewUserParticipateEnable(groupId);
        checkUserAlreadyParticipatedInGroup(user, userGroup);

        groupAndUserRepository.save(GroupAndUser.of(user, userGroup));
    }

    @CachePut(value = "group:group-id", key = "#updateGroupRequestDto.groupId")
    @OptimisticLock
    @Transactional
    public void updateGroup(final UpdateGroupRequestDto updateGroupRequestDto) {

        UserGroup userGroup = groupRepository.getById(updateGroupRequestDto.getGroupId());
        userGroup.update(updateGroupRequestDto.getGroupName(), updateGroupRequestDto.getGroupType(), updateGroupRequestDto.getGroupIcon());
    }


    @CacheEvict(value = "group:group-id", key = "#groupId")
    @Transactional
    public void removeGroup(final Long userId, final Long groupId) {

        UserGroup userGroup = groupRepository.getById(groupId);
        userGroup.remove();
    }


    @CacheEvict(value = "group:group-id", key = "#groupId")
    @Transactional
    public void exitGroup(final Long userId, final Long groupId) {

        int particiatedUserCount = groupAndUserRepository.countUserParticipatedInGroup(groupId);

        if (particiatedUserCount < 2) {
            removeGroup(userId, groupId);
        }

        groupAndUserRepository.deleteGroupAndUserByUserIdAndGroupId(userId, groupId);
    }


    private void checkUserAlreadyParticipatedInGroup(User user, UserGroup userGroup) {
        if (groupAndUserRepository.findGroupAndUserByUserAndGroup(user, userGroup).isPresent()) {
            throw new BusinessException(ErrorCode.DUPLICATED_GROUP);
        }
    }


    private void validateNewUserParticipateEnable(Long groupId) {
        int particiatedUserCount = groupAndUserRepository.countUserParticipatedInGroup(groupId);

        if (particiatedUserCount > GROUP_MEMBER_MAX_COUNT) {
            throw new BusinessException(ErrorCode.PARTICIPATE_GROUP_NOT_POSSIBLE);
        }
    }
}
