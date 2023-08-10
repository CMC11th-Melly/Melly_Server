package cmc.mellyserver.mellycore.group.application;


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
    public GroupDetailResponseDto getGroupDetail(Long groupId) {

        UserGroup userGroup = groupRepository.findById(groupId).orElseThrow(() -> new BusinessException(ErrorCode.DUPLICATED_GROUP));
        List<User> usersParticipatedInGroup = groupAndUserRepository.getUsersParticipatedInGroup(groupId);

        return GroupDetailResponseDto.of(userGroup, usersParticipatedInGroup);
    }

    /*
    캐시 적용 여부 : 불필요
     */
    @Transactional(readOnly = true)
    public Slice<GroupLoginUserParticipatedResponseDto> findGroupListLoginUserParticiated(Long userId, Long groupId, Pageable pageable) {

        return userGroupQueryRepository.getGroupListLoginUserParticipate(userId, groupId, pageable);
    }

    /*
    캐시 적용 여부 : 불가능
    이유 : 메모리 추가 시에는 유저가 속해있는 그룹 정보가 항상 최신으로 제공되야 한다
    */
    @Transactional(readOnly = true)
    public Slice<GroupLoginUserParticipatedResponseDto> findGroupListLoginUserParticipateForMemoryCreate(Long userId, Long groupId, Pageable pageable) {
        return userGroupQueryRepository.getGroupListLoginUserParticipate(userId, groupId, pageable);
    }

    @Transactional
    public Long saveGroup(CreateGroupRequestDto createGroupRequestDto) {

        User user = userRepository.findById(createGroupRequestDto.getCreatorId()).orElseThrow(() -> {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        });

        UserGroup savedGroup = groupRepository.save(createGroupRequestDto.toEntity());
        groupAndUserRepository.save(GroupAndUser.of(user, savedGroup));
        return savedGroup.getId();
    }


    @CacheEvict(value = "group:group-id", key = "#groupId")
    @Transactional
    public void participateToGroup(Long userId, Long groupId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        UserGroup userGroup = groupRepository.findById(groupId).orElseThrow(() -> new BusinessException(ErrorCode.NO_SUCH_GROUP));
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
    public void updateGroup(Long id, UpdateGroupRequestDto updateGroupRequestDto) {


        UserGroup userGroup = groupRepository.findById(updateGroupRequestDto.getGroupId()).orElseThrow(() -> {
            throw new BusinessException(ErrorCode.NO_SUCH_GROUP);
        });

        userGroup.update(updateGroupRequestDto.getGroupName(), updateGroupRequestDto.getGroupType(), updateGroupRequestDto.getGroupIcon());
    }

    @CacheEvict(value = "group:group-id", key = "#groupId")
    @Transactional
    public void removeGroup(Long id, Long groupId) {

        UserGroup userGroup = groupRepository.findById(groupId).orElseThrow(() -> {
            throw new BusinessException(ErrorCode.NO_SUCH_GROUP);
        });
        userGroup.remove();
    }

    @CacheEvict(value = "group:group-id", key = "#groupId")
    @Transactional
    public void exitGroup(Long userId, Long groupId) {

        Integer particiatedUserCount = groupAndUserRepository.countUserParticipatedInGroup(groupId);

        log.info("count : {}", particiatedUserCount);

        // 그룹에 관리자는 무조건 한명 존재해야 한다.
        // 기본 구성 관리자 1 + 일반 유저 1

        // 그룹 탈퇴
        // 1. 일반 유저는 바로 탈퇴 가능하다
        // 2. 관리자는 그룹에 관리자가 2명 이상 존재할때 탈퇴할 수 있다.

        // 권한 변경
        // 1. 관리자는 일반 유저를 관리자로 권한 변경할 수 있다.
        // 2. 관리자는 다른 관리자를 일반 유저로 강등도 가능하다.
        // 3. 그룹에 관리자는 무조건 한명 이상 존재해야 한다.


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
