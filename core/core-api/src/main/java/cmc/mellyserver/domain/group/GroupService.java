package cmc.mellyserver.domain.group;


import cmc.mellyserver.common.aop.lock.annotation.DistributedLock;
import cmc.mellyserver.common.aop.lock.annotation.OptimisticLock;
import cmc.mellyserver.dbcore.group.GroupAndUser;
import cmc.mellyserver.dbcore.group.UserGroup;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.domain.group.dto.request.CreateGroupRequestDto;
import cmc.mellyserver.domain.group.dto.request.UpdateGroupRequestDto;
import cmc.mellyserver.domain.group.dto.response.GroupListLoginUserParticipatedResponse;
import cmc.mellyserver.domain.group.query.dto.GroupDetailResponseDto;
import cmc.mellyserver.domain.group.query.dto.GroupLoginUserParticipatedResponseDto;
import cmc.mellyserver.domain.user.UserReader;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
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

    private final GroupReader groupReader;

    private final GroupWriter groupWriter;

    private final UserReader userReader;

    private final GroupAndUserReader groupAndUserReader;

    private final GroupAndUserWriter groupAndUserWriter;


    @Cacheable(value = "group:group-id", key = "#groupId")
    @Transactional(readOnly = true)
    public GroupDetailResponseDto getGroupDetail(final Long groupId) {

        UserGroup userGroup = groupReader.findById(groupId);
        List<User> groupMembers = groupAndUserReader.getGroupMembers(groupId);
        return GroupDetailResponseDto.of(userGroup, groupMembers);
    }


    @Transactional(readOnly = true)
    public GroupListLoginUserParticipatedResponse findGroupListLoginUserParticiated(final Long userId, final Long groupId, final Pageable pageable) {

        Slice<GroupLoginUserParticipatedResponseDto> groupListLoginUserParticipate = groupReader.groupListLoginUserParticipate(userId, groupId, pageable);
        return transferToList(groupListLoginUserParticipate);
    }


    @Transactional
    public Long saveGroup(final CreateGroupRequestDto createGroupRequestDto) {

        User user = userReader.findById(createGroupRequestDto.getCreatorId());
        UserGroup savedGroup = groupWriter.save(createGroupRequestDto.toEntity());
        return groupAndUserWriter.save(GroupAndUser.of(user, savedGroup)).getId();
    }


    @CachePut(value = "group:group-id", key = "#groupId")
    @DistributedLock(key = "#groupId", waitTime = 10L, leaseTime = 2L)
    @OptimisticLock
    @Transactional
    public void participateToGroup(final Long userId, final Long groupId) {

        User user = userReader.findById(userId);
        UserGroup userGroup = groupReader.findById(groupId);

        validateNewUserParticipateEnable(groupId);
        checkUserAlreadyParticipatedInGroup(user.getId(), userGroup.getId());

        groupAndUserWriter.save(GroupAndUser.of(user, userGroup));
    }


    @CachePut(value = "group:group-id", key = "#updateGroupRequestDto.groupId")
    @OptimisticLock
    @Transactional
    public void updateGroup(final UpdateGroupRequestDto updateGroupRequestDto) {

        UserGroup userGroup = groupReader.findById(updateGroupRequestDto.getGroupId());
        userGroup.update(updateGroupRequestDto.getGroupName(), updateGroupRequestDto.getGroupType(), updateGroupRequestDto.getGroupIcon());
    }


    @CacheEvict(value = "group:group-id", key = "#groupId")
    @Transactional
    public void removeGroup(final Long groupId) {

        UserGroup userGroup = groupReader.findById(groupId);
        userGroup.remove();
    }


    @CacheEvict(value = "group:group-id", key = "#groupId")
    @Transactional
    public void exitGroup(final Long userId, final Long groupId) {

        if (groupAndUserReader.countGroupMembers(groupId) < 2) {
            removeGroup(groupId);
        }

        groupAndUserWriter.deleteByUserIdAndGroupId(userId, groupId);
    }


    private void checkUserAlreadyParticipatedInGroup(final Long userId, final Long groupId) {

        if (groupAndUserReader.findByUserIdAndGroupId(userId, groupId).isPresent()) {
            throw new BusinessException(ErrorCode.DUPLICATED_GROUP);
        }
    }


    private void validateNewUserParticipateEnable(Long groupId) {

        if (groupAndUserReader.countGroupMembers(groupId) > GROUP_MEMBER_MAX_COUNT) {
            throw new BusinessException(ErrorCode.PARTICIPATE_GROUP_NOT_POSSIBLE);
        }
    }


    private GroupListLoginUserParticipatedResponse transferToList(Slice<GroupLoginUserParticipatedResponseDto> groupListLoginUserParticipate) {

        List<GroupLoginUserParticipatedResponseDto> contents = groupListLoginUserParticipate.getContent();
        boolean next = groupListLoginUserParticipate.hasNext();
        return GroupListLoginUserParticipatedResponse.from(contents, next);
    }

}
