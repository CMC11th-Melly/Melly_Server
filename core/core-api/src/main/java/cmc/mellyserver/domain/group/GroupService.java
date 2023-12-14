package cmc.mellyserver.domain.group;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cmc.mellyserver.common.aspect.lock.DistributedLock;
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

@Service
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
        return groupAndUserWriter.save(GroupAndUser.of(user, savedGroup)).getId();
    }

    @DistributedLock(key = "#groupId")
    @Transactional
    public void joinGroup(final Long userId, final Long groupId) {

        User user = userReader.findById(userId);
        UserGroup userGroup = groupReader.read(groupId);
        groupValidator.isMaximumGroupMember(groupId);
        groupValidator.isDuplicatedJoin(user.getId(), userGroup.getId());
        groupAndUserWriter.save(GroupAndUser.of(user, userGroup));
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

    @CacheEvict(cacheNames = CacheNames.GROUP, key = "#groupId")
    @Transactional
    public void exitGroup(final Long userId, final Long groupId) {

        groupAndUserWriter.deleteByUserIdAndGroupId(userId, groupId);
        if (groupValidator.isGroupRemovable(groupId)) {
            UserGroup userGroup = groupReader.read(groupId);
            userGroup.delete();
        }
    }

    private void checkRemoveAuthority(Long userId, UserGroup userGroup) {
        if (!userGroup.checkAuthority(userId)) {
            throw new BusinessException(ErrorCode.NO_AUTHORITY_TO_REMOVE);
        }
    }
}
