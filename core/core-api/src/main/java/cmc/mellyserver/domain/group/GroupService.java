package cmc.mellyserver.domain.group;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cmc.mellyserver.dbcore.group.GroupAndUser;
import cmc.mellyserver.dbcore.group.UserGroup;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.domain.group.dto.GroupMemberResponseDto;
import cmc.mellyserver.domain.group.dto.request.CreateGroupRequestDto;
import cmc.mellyserver.domain.group.dto.request.UpdateGroupRequestDto;
import cmc.mellyserver.domain.group.dto.response.GroupListLoginUserParticipatedResponse;
import cmc.mellyserver.domain.group.query.dto.GroupDetailResponseDto;
import cmc.mellyserver.domain.user.UserReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

	@Cacheable(value = "group:group-id", key = "#groupId")
	public GroupDetailResponseDto getGroupDetail(final Long groupId, final Long userId) {

		UserGroup userGroup = groupReader.findById(groupId); // 그룹을 찾는다
		List<GroupMemberResponseDto> groupMembers = groupAndUserReader.getGroupMembers(groupId,
			userId);// 그룹에 속한 멤버들을 찾는다
		return GroupDetailResponseDto.of(userGroup, groupMembers);
	}

	public GroupListLoginUserParticipatedResponse findUserParticipatedGroups(final Long userId, final Long lastId,
		final Pageable pageable) {

		return groupReader.groupListLoginUserParticipate(userId, lastId, pageable);
	}

	@Transactional
	public Long saveGroup(final CreateGroupRequestDto createGroupRequestDto) {

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

	@CachePut(value = "group:group-id", key = "#updateGroupRequestDto.groupId")
	@Transactional
	public void updateGroup(final UpdateGroupRequestDto updateGroupRequestDto) {

		UserGroup userGroup = groupReader.findById(updateGroupRequestDto.getGroupId());
		userGroup.update(updateGroupRequestDto.getGroupName(), updateGroupRequestDto.getGroupType(),
			updateGroupRequestDto.getGroupIcon());
	}

	@CacheEvict(value = "group:group-id", key = "#groupId")
	@Transactional
	public void removeGroup(final Long groupId) {

		UserGroup userGroup = groupReader.findById(groupId);
		userGroup.delete();
	}

	@CacheEvict(value = "group:group-id", key = "#groupId")
	@Transactional
	public void exitGroup(final Long userId, final Long groupId) {

		if (groupValidator.isGroupRemovable(groupId)) {
			UserGroup userGroup = groupReader.findById(groupId);
			userGroup.delete();
		}
		groupAndUserWriter.deleteByUserIdAndGroupId(userId, groupId);
	}
}
