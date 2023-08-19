package cmc.mellyserver.mellycore.group.application;


import cmc.mellyserver.mellycore.common.aop.DistributedLock;
import cmc.mellyserver.mellycore.common.aop.OptimisticLock;
import cmc.mellyserver.mellycore.common.exception.BusinessException;
import cmc.mellyserver.mellycore.common.exception.ErrorCode;
import cmc.mellyserver.mellycore.group.application.dto.request.CreateGroupRequestDto;
import cmc.mellyserver.mellycore.group.application.dto.request.UpdateGroupRequestDto;
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

    private final GroupRepository groupRepository;

    private final GroupAndUserRepository groupAndUserRepository;

    private final UserRepository userRepository;

    private final UserGroupQueryRepository userGroupQueryRepository;


    /*
    캐시 적용 여부 : 가능
     */
    @Cacheable(value = "group:group-id", key = "#groupId")
    @Transactional(readOnly = true)
    public GroupDetailResponseDto getGroupDetail(final Long groupId) {

        UserGroup userGroup = groupRepository.getById(groupId);
        List<User> usersParticipatedInGroup = groupAndUserRepository.getUsersParticipatedInGroup(groupId);
        return GroupDetailResponseDto.of(userGroup, usersParticipatedInGroup);
    }


    /*
    캐시 적용 여부 : 불가능
    이유 : 정확한 데이터를 반영해야함으로 직접 DB에서 조회해야 한다.
     */
    @Transactional(readOnly = true)
    public Slice<GroupLoginUserParticipatedResponseDto> findGroupListLoginUserParticiated(final Long userId, final Long groupId, final Pageable pageable) {

        return userGroupQueryRepository.getGroupListLoginUserParticipate(userId, groupId, pageable);
    }


    /*
    캐시 적용 여부 : 불가능
    이유 : 메모리 추가 시에는 유저가 속해있는 그룹 정보가 항상 최신으로 제공되야 한다
    */
    @Transactional(readOnly = true)
    public Slice<GroupLoginUserParticipatedResponseDto> findGroupListLoginUserParticipateForMemoryCreate(final Long userId, final Long groupId, final Pageable pageable) {
        return userGroupQueryRepository.getGroupListLoginUserParticipate(userId, groupId, pageable);
    }

    // 관리자 권한을 가진 사람, 멤버 초대와 그룹 수정 가능

    @Transactional
    public Long saveGroup(final CreateGroupRequestDto createGroupRequestDto) {

        User user = userRepository.getById(createGroupRequestDto.getCreatorId());
        UserGroup savedGroup = groupRepository.save(createGroupRequestDto.toEntity());
        groupAndUserRepository.save(GroupAndUser.of(user, savedGroup));
        return savedGroup.getId();
    }


    @CacheEvict(value = "group:group-id", key = "#groupId")
    @DistributedLock(key = "#groupId", waitTime = 3L, leaseTime = 2L)
    @Transactional
    public void participateToGroup(final Long userId, final Long groupId) {

        User user = userRepository.getById(userId);
        UserGroup userGroup = groupRepository.getById(groupId);
        Integer particiatedUserCount = groupAndUserRepository.countUserParticipatedInGroup(groupId);

        if (particiatedUserCount > 9) {
            throw new BusinessException(ErrorCode.PARTICIPATE_GROUP_NOT_POSSIBLE);
        }

        checkUserAlreadyParticipatedInGroup(user, userGroup);
        groupAndUserRepository.save(GroupAndUser.of(user, userGroup));
    }


    @OptimisticLock
    @CacheEvict(value = "group:group-id", key = "#updateGroupRequestDto.groupId")
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

        Integer particiatedUserCount = groupAndUserRepository.countUserParticipatedInGroup(groupId);

        log.info("count : {}", particiatedUserCount);


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


}
