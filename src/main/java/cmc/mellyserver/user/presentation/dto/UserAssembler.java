package cmc.mellyserver.user.presentation.dto;

import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.memory.domain.dto.MemoryResponseDto;


import java.util.List;

public class UserAssembler {

//    public static Page<UserCreatedMemoryListResponse> toUserCreatedMemoryListResponse(Page<UserCreatedMemoryListResponseDto> memories)
//    {
//        return memories.map(m -> new UserCreatedMemoryListResponse(m.getPlaceId(),m.getPlaceName(),m.getMemoryId(),m.getMemoryImages(),
//                m.getTitle(),m.getContent(),m.getGroupType(),m.getGroupName(),m.getStars(),m.getKeyword(),m.isLoginUserWrite(),m.getVisitedDate()
//        ));
//    }



    // TODO : 조치 필요
    public static List<MemoryResponseDto> getUserGroupResponses(List<UserGroup> userGroups, Long userSeq)
    {
//        return userGroups.stream().map(ug -> new GetUserGroupResponse(ug.getId(),
//                ug.getGroupIcon(),
//                ug.getGroupName() == null ? "" : ug.getGroupName(),
//                ug.getGroupAndUsers().stream().map(gu -> new UserDto(
//                        gu.getUser().getUserSeq(),
//                        gu.getUser().getProfileImage(),
//                        gu.getUser().getNickname(),
//                        (gu.getUser().getUserId().equals(userId)) ? true : false
//                )).collect(Collectors.toList()),
//                ug.getGroupType() == null ? GroupType.ALL : ug.getGroupType(),
//                ug.getInviteLink()
//                )).collect(Collectors.toList());
        return null;
    }
}
