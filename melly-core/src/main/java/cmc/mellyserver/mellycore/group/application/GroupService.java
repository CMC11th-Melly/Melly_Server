package cmc.mellyserver.mellycore.group.application;

import cmc.mellyserver.mellycommon.codes.ErrorCode;
import cmc.mellyserver.mellycore.common.exception.GlobalBadRequestException;
import cmc.mellyserver.mellycore.group.application.dto.request.CreateGroupRequestDto;
import cmc.mellyserver.mellycore.group.application.dto.request.UpdateGroupRequestDto;
import cmc.mellyserver.mellycore.group.domain.GroupAndUser;
import cmc.mellyserver.mellycore.group.domain.UserGroup;
import cmc.mellyserver.mellycore.group.domain.repository.GroupAndUserRepository;
import cmc.mellyserver.mellycore.group.domain.repository.GroupRepository;
import cmc.mellyserver.mellycore.group.domain.repository.UserGroupQueryRepository;
import cmc.mellyserver.mellycore.group.domain.repository.dto.GroupLoginUserParticipatedResponseDto;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    @Cacheable(value = "group", key = "#groupId")
    @Transactional(readOnly = true)
    public UserGroup findGroupById(Long groupId) {

        return groupRepository.findById(groupId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ErrorCode.NO_SUCH_GROUP);
        });
    }

    /*
    캐시 적용 여부 : 가능
     */
    @Cacheable(value = "groupList", key = "#userSeq")
    @Transactional(readOnly = true)
    public List<GroupLoginUserParticipatedResponseDto> findGroupListLoginUserParticiated(Long userSeq) {

        return userGroupQueryRepository.getGroupListLoginUserParticipate(userSeq);
    }

    /*
    캐시 적용 여부 : 불가능
    이유 : 메모리 추가 시에는 유저가 속해있는 그룹 정보가 항상 최신으로 제공되야 한다
    */
    @Transactional(readOnly = true)
    public List<GroupLoginUserParticipatedResponseDto> findGroupListLoginUserParticipateForMemoryCreate(Long userSeq) {
        return userGroupQueryRepository.getGroupListLoginUserParticipate(userSeq);
    }

    @Transactional
    public UserGroup saveGroup(CreateGroupRequestDto createGroupRequestDto) {

        UserGroup userGroup = createGroupRequestDto.toEntity();
        userGroup.assignGroupManager(createGroupRequestDto.getUserSeq());
        return groupRepository.save(userGroup);
    }

    /*
    해당 유저가 속해있는 그룹 리스트를 Eviction 해준다
     */
    @CacheEvict(value = "groupList", key = "#userSeq")
    @Transactional
    public void participateToGroup(Long userSeq, Long groupId) {

        User user = userRepository.findById(userSeq).orElseThrow(() -> {
            throw new GlobalBadRequestException(ErrorCode.NO_SUCH_USER);
        });

        UserGroup userGroup = groupRepository.findById(groupId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ErrorCode.NO_SUCH_GROUP);
        });

        checkUserAlreadyParticipatedInGroup(user, userGroup);

        groupAndUserRepository.save(GroupAndUser.of(user, userGroup));
    }

    @CacheEvict(value = "group", key = "#updateGroupRequestDto.groupId")
    @Transactional
    public void updateGroup(Long userSeq, UpdateGroupRequestDto updateGroupRequestDto) {

        UserGroup userGroup = groupRepository.findById(updateGroupRequestDto.getGroupId()).orElseThrow(() -> {
            throw new GlobalBadRequestException(ErrorCode.NO_SUCH_GROUP);
        });
        userGroup.update(userSeq, updateGroupRequestDto.getGroupName(), updateGroupRequestDto.getGroupType(), updateGroupRequestDto.getGroupIcon());
    }

    @CacheEvict(value = "groupList", key = "#updateGroupRequestDto.groupId", cacheManager = "redisCacheManager", allEntries = true)
    @Transactional
    public void removeGroup(Long userSeq, Long groupId) {

        UserGroup userGroup = groupRepository.findById(groupId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ErrorCode.NO_SUCH_GROUP);
        });
        userGroup.remove(userSeq);
    }

    private void checkUserAlreadyParticipatedInGroup(User user, UserGroup userGroup) {
        if (groupAndUserRepository.findGroupAndUserByUserAndGroup(user, userGroup).isPresent()) {
            throw new GlobalBadRequestException(ErrorCode.DUPLICATED_GROUP);
        }
    }
}
