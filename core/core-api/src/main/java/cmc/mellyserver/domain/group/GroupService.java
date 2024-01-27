package cmc.mellyserver.domain.group;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public GroupResponseDto getGroup(Long userId, Long groupId) {

        UserGroup userGroup = groupReader.read(groupId);
        List<GroupMemberResponseDto> groupMembers = groupAndUserReader.getGroupMembers(userId, groupId);
        return GroupResponseDto.of(userGroup, groupMembers);
    }

    public UserJoinedGroupsResponse findUserParticipatedGroups(Long userId, Long lastId, Pageable pageable) {
        return groupReader.groupListLoginUserParticipate(userId, lastId, pageable);
    }

    @Transactional
    public Long saveGroup(Long userId, CreateGroupRequestDto createGroupRequestDto) {

        User user = userReader.findById(userId);
        UserGroup savedGroup = groupWriter.save(user.getId(), createGroupRequestDto.toEntity());
        return groupAndUserWriter.save(GroupAndUser.of(user.getId(), savedGroup)).getId();
    }

    @OptimisticLock
    @CacheEvict(cacheNames = CacheNames.GROUP, key = "#groupId")
    @Transactional
    public void joinGroup(Long userId, Long groupId) {

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
    public void removeGroup(Long userId, Long groupId) {

        UserGroup userGroup = groupReader.read(groupId);
        groupValidator.checkRemoveAuthority(userId, userGroup);
        userGroup.delete();
    }

    @CacheEvict(cacheNames = CacheNames.GROUP, key = "#groupId")
    @Transactional
    public void exitGroup(Long userId, Long groupId) {
        groupAndUserWriter.deleteByUserIdAndGroupId(userId, groupId);
    }

}
