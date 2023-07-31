package cmc.mellyserver.mellyapi.common.factory;

import cmc.mellyserver.mellyapi.group.presentation.dto.request.GroupCreateRequest;
import cmc.mellyserver.mellyapi.group.presentation.dto.request.GroupUpdateRequest;
import cmc.mellyserver.mellycommon.enums.DeleteStatus;
import cmc.mellyserver.mellycommon.enums.GroupType;
import cmc.mellyserver.mellycore.group.application.dto.request.CreateGroupRequestDto;
import cmc.mellyserver.mellycore.group.application.dto.request.UpdateGroupRequestDto;
import cmc.mellyserver.mellycore.group.domain.UserGroup;

public class UserGroupFactory {

    public static UserGroup mockUserGroup() {
        return new UserGroup(1L, "동기", 1, "http://naver.com", 2L, GroupType.FRIEND, DeleteStatus.N);
    }

    public static UserGroup userGroup() {
        return UserGroup.builder()
                .groupName("테스트 그룹")
                .groupType(GroupType.FRIEND)
                .groupIcon(1)
                .inviteLink("http://naver.com")
                .build();
    }

    public static GroupCreateRequest mockGroupCreateRequest() {
        return GroupCreateRequest.builder()
                .groupName("테스트 그룹")
                .groupIcon(1)
                .groupType(GroupType.FRIEND)
                .build();
    }

    public static GroupUpdateRequest mockGroupUpdateRequest() {
        return GroupUpdateRequest.builder()
                .groupName("테스트 그룹")
                .groupIcon(1)
                .groupType(GroupType.FRIEND)
                .build();
    }

    public static CreateGroupRequestDto mockCreateGroupRequestDto() {
        return CreateGroupRequestDto.builder()
                .id(2L)
                .groupIcon(1)
                .groupName("테스트 그룹")
                .groupType(GroupType.FRIEND)
                .build();
    }

    public static UpdateGroupRequestDto mockUpdateGroupRequestDto() {
        return UpdateGroupRequestDto.builder()
                .groupId(1L)
                .groupIcon(2)
                .groupName("수정된 그룹 이름")
                .groupType(GroupType.COMPANY)
                .build();
    }

}
