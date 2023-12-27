package cmc.mellyserver.domain.group;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cmc.mellyserver.common.aspect.lock.DistributedLock;
import cmc.mellyserver.common.aspect.lock.OptimisticLock;
import cmc.mellyserver.config.cache.CacheNames;
import cmc.mellyserver.dbcore.group.GroupAndUser;
import cmc.mellyserver.dbcore.group.UserGroup;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.domain.group.dto.GroupMemberResponseDto;
import cmc.mellyserver.domain.group.dto.request.CreateGroupRequestDto;
import cmc.mellyserver.domain.group.dto.request.UpdateGroupRequestDto;
import cmc.mellyserver.domain.group.dto.response.UserJoinedGroupsResponse;
import cmc.mellyserver.domain.group.query.dto.GroupResponseDto;
import cmc.mellyserver.domain.user.UserReader;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupService {

    private final GroupReader groupReader;

    private final GroupWriter groupWriter;

    private final UserReader userReader;

    private final GroupAndUserReader groupAndUserReader;

    private final GroupAndUserWriter groupAndUserWriter;

    private final GroupValidator groupValidator;

    @Cacheable(cacheNames = CacheNames.GROUP, key = "#groupId")
    public GroupResponseDto getGroup(final Long userId, final Long groupId) {

        UserGroup userGroup = groupReader.read(groupId);
        List<GroupMemberResponseDto> groupMembers = groupAndUserReader.getGroupMembers(userId, groupId);
        return GroupResponseDto.of(userGroup, groupMembers);
    }

    public UserJoinedGroupsResponse findUserParticipatedGroups(final Long userId, final Long lastId,
        final Pageable pageable) {

        return groupReader.groupListLoginUserParticipate(userId, lastId, pageable);
    }

    @Transactional
    public Long saveGroup(Long userId, CreateGroupRequestDto createGroupRequestDto) {

        User user = userReader.findById(userId);
        UserGroup savedGroup = groupWriter.save(user.getId(), createGroupRequestDto.toEntity());
        return groupAndUserWriter.save(GroupAndUser.of(user.getId(), savedGroup)).getId();
    }

    // @DistributedLock(key = "#groupId")
    @OptimisticLock(retryCount = 3, waitTime = 1000L) // 분산락을 획득한 트랜잭션이 커밋 전에 락을 풀어버리는 상황에 대비
    @CacheEvict(cacheNames = CacheNames.GROUP, key = "#groupId")
    @Transactional
    public void joinGroup(final Long userId, final Long groupId) {

        UserGroup userGroup = groupReader.readWithLock(groupId);
        groupValidator.isMaximumGroupMember(groupId);
        groupValidator.isDuplicatedJoin(userId, userGroup.getId());
        groupAndUserWriter.save(GroupAndUser.of(userId, userGroup));
    }

    @CacheEvict(cacheNames = CacheNames.GROUP, key = "#updateGroupRequestDto.groupId")
    @Transactional
    public void updateGroup(UpdateGroupRequestDto updateGroupRequestDto) {

        UserGroup userGroup = groupReader.read(updateGroupRequestDto.getGroupId());
        userGroup.update(updateGroupRequestDto.getGroupName(), updateGroupRequestDto.getGroupType(),
            updateGroupRequestDto.getGroupIcon());
    }

    @CacheEvict(cacheNames = CacheNames.GROUP, key = "#groupId")
    @Transactional
    public void removeGroup(final Long userId, final Long groupId) {

        UserGroup userGroup = groupReader.read(groupId);
        checkRemoveAuthority(userId, userGroup);
        userGroup.delete();
    }

    @DistributedLock(key = "#groupId")
    @CacheEvict(cacheNames = CacheNames.GROUP, key = "#groupId")
    @Transactional
    public void exitGroup(final Long userId, final Long groupId) {
        groupAndUserWriter.deleteByUserIdAndGroupId(userId, groupId);
    }

    private void checkRemoveAuthority(Long userId, UserGroup userGroup) {
        if (!userGroup.checkAuthority(userId)) {
            throw new BusinessException(ErrorCode.NO_AUTHORITY_TO_REMOVE);
        }
    }
}
