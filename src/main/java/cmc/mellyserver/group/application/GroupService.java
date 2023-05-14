package cmc.mellyserver.group.application;

import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.group.domain.GroupAndUser;
import cmc.mellyserver.group.domain.repository.GroupAndUserRepository;
import cmc.mellyserver.group.domain.repository.GroupRepository;
import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.group.presentation.dto.request.GroupCreateRequest;
import cmc.mellyserver.group.presentation.dto.request.GroupUpdateRequest;
import cmc.mellyserver.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupAndUserRepository groupAndUserRepository;
    private final AuthenticatedUserChecker authenticatedUserChecker;


    public UserGroup getGroupById(Long groupId)
    {
      return groupRepository.findById(groupId).orElseThrow(()-> {throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_GROUP);});
    }



    @Transactional
    public UserGroup saveGroup(Long userSeq, GroupCreateRequest groupCreateRequest)
    {
        UserGroup userGroup = new UserGroup(groupCreateRequest.getGroupName(), "-", groupCreateRequest.getGroupType(), groupCreateRequest.getGroupIcon(), userSeq);
        userGroup.setCreatorId(userSeq);
        return groupRepository.save(userGroup);
    }



    @Transactional
    public void participateToGroup(Long userSeq, Long groupId)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(userSeq); // 1. 유저 찾기

        UserGroup userGroup = groupRepository.findById(groupId).orElseThrow(() -> { // 2. group 만들기
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_GROUP);
        });
        // 3. group_and_user 만들기
        if(groupAndUserRepository.findGroupAndUserByUserAndGroup(user, userGroup).isPresent()) throw new GlobalBadRequestException(ExceptionCodeAndDetails.DUPLICATED_GROUP);
        // 4. 저장
        groupAndUserRepository.save(new GroupAndUser(user, userGroup));
    }



    @Transactional
    public void updateGroup(Long groupId, GroupUpdateRequest groupUpdateRequestDto) {

        UserGroup userGroup = groupRepository.findById(groupId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_GROUP);
        });
        userGroup.updateUserGroup(groupUpdateRequestDto.getGroupName(),groupUpdateRequestDto.getGroupType(),groupUpdateRequestDto.getGroupIcon());

    }



    @Transactional
    public String deleteGroup(Long userSeq, Long groupId)
    {
        UserGroup userGroup = groupRepository.findById(groupId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_GROUP);
        });


        // 만약 해당 그룹을 만든 사람이라면 삭제 가능
        if (userGroup.getCreatorId().equals(userSeq))
        {
            userGroup.remove();
            return "그룹 삭제 완료";
        }
        return "삭제 권한이 없습니다";
    }

}
