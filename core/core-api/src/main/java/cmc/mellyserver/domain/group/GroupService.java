package cmc.mellyserver.domain.group;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cmc.mellyserver.config.cache.CacheNames;
import cmc.mellyserver.dbcore.group.GroupAndUser;
import cmc.mellyserver.dbcore.group.UserGroup;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.domain.group.dto.GroupMemberResponseDto;
import cmc.mellyserver.domain.group.dto.request.CreateGroupRequestDto;
import cmc.mellyserver.domain.group.dto.request.UpdateGroupRequestDto;
import cmc.mellyserver.domain.group.dto.response.UserJoinedGroupsResponse;
import cmc.mellyserver.domain.group.query.dto.GroupDetailResponseDto;
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
    public GroupDetailResponseDto getGroup(final Long userId, final Long groupId) {

        UserGroup userGroup = groupReader.findById(groupId);
        List<GroupMemberResponseDto> groupMembers = groupAndUserReader.getGroupMembers(userId, groupId);
        return GroupDetailResponseDto.of(userGroup, groupMembers);
    }

    public UserJoinedGroupsResponse findUserParticipatedGroups(final Long userId, final Long lastId,
        final Pageable pageable) {

        return groupReader.groupListLoginUserParticipate(userId, lastId, pageable);
    }

    @Transactional
    public Long saveGroup(CreateGroupRequestDto createGroupRequestDto) {

        User user = userReader.findById(createGroupRequestDto.getCreatorId());
        UserGroup savedGroup = groupWriter.save(createGroupRequestDto.toEntity());
        return groupAndUserWriter.save(GroupAndUser.of(user, savedGroup)).getId();
    }

    @Transactional
    public void joinGroup(final Long userId, final Long groupId) {

        User user = userReader.findById(userId);
        UserGroup userGroup = groupReader.findById(groupId);

        groupValidator.isMaximumGroupMember(groupId);
        groupValidator.isDuplicatedJoin(user.getId(), userGroup.getId());

        groupAndUserWriter.save(GroupAndUser.of(user, userGroup));
    }

    @CacheEvict(cacheNames = CacheNames.GROUP, key = "#updateGroupRequestDto.groupId")
    @Transactional
    public void updateGroup(UpdateGroupRequestDto updateGroupRequestDto) {

        UserGroup userGroup = groupReader.findById(updateGroupRequestDto.getGroupId());
        userGroup.update(updateGroupRequestDto.getGroupName(), updateGroupRequestDto.getGroupType(),
            updateGroupRequestDto.getGroupIcon());
    }

    @CacheEvict(cacheNames = CacheNames.GROUP, key = "#groupId")
    @Transactional
    public void removeGroup(final Long groupId) {

        UserGroup userGroup = groupReader.findById(groupId);
        userGroup.delete();
    }

    @CacheEvict(cacheNames = CacheNames.GROUP, key = "#groupId")
    @Transactional
    public void exitGroup(final Long userId, final Long groupId) {

        if (groupValidator.isGroupRemovable(groupId)) {
            UserGroup userGroup = groupReader.findById(groupId);
            userGroup.delete();
        }
        groupAndUserWriter.deleteByUserIdAndGroupId(userId, groupId);
    }
}
