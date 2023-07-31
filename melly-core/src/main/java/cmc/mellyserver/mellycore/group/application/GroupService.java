package cmc.mellyserver.mellycore.group.application;

import cmc.mellyserver.mellycommon.codes.ErrorCode;
import cmc.mellyserver.mellycore.common.exception.BusinessException;
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
    public void saveGroup(CreateGroupRequestDto createGroupRequestDto) {

        User user = userRepository.findById(createGroupRequestDto.getCreatorId()).orElseThrow(() -> {
            throw new BusinessException(ErrorCode.NO_SUCH_USER);
        });

        UserGroup savedGroup = groupRepository.save(createGroupRequestDto.toEntity());
        groupAndUserRepository.save(GroupAndUser.of(user, savedGroup));

        // redis에 해당 그룹id로 key와 등록된 유저 id set으로 등록
    }


    @CacheEvict(value = "group:group-id", key = "#groupId")
    @Transactional
    public void participateToGroup(Long userId, Long groupId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(ErrorCode.NO_SUCH_USER));
        UserGroup userGroup = groupRepository.findById(groupId).orElseThrow(() -> new BusinessException(ErrorCode.NO_SUCH_GROUP));
        Integer particiatedUserCount = groupAndUserRepository.countUserParticipatedInGroup(groupId);

        if (particiatedUserCount > 9) {
            throw new BusinessException(ErrorCode.PARTICIPATE_GROUP_NOT_POSSIBLE);
        }

        checkUserAlreadyParticipatedInGroup(user, userGroup);

        groupAndUserRepository.save(GroupAndUser.of(user, userGroup));
    }

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

        // 현재 그룹에 남아있는 사람이 혼자라면, 본인이 탈퇴시 그룹은 삭제된다.
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
