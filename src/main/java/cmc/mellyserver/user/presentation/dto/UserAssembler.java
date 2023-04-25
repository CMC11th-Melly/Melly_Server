package cmc.mellyserver.user.presentation.dto;

import cmc.mellyserver.group.domain.group.UserGroup;
import cmc.mellyserver.common.enums.GroupType;
import cmc.mellyserver.memory.domain.dto.UserCreatedMemoryListResponseDto;
import cmc.mellyserver.memory.presentation.dto.common.ImageDto;
import cmc.mellyserver.user.presentation.dto.common.UserDto;
import cmc.mellyserver.user.presentation.dto.response.GetUserGroupResponse;
import cmc.mellyserver.user.presentation.dto.response.UserCreatedMemoryListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

public class UserAssembler {

//    public static Page<UserCreatedMemoryListResponse> toUserCreatedMemoryListResponse(Page<UserCreatedMemoryListResponseDto> memories)
//    {
//        return memories.map(m -> new UserCreatedMemoryListResponse(m.getPlaceId(),m.getPlaceName(),m.getMemoryId(),m.getMemoryImages(),
//                m.getTitle(),m.getContent(),m.getGroupType(),m.getGroupName(),m.getStars(),m.getKeyword(),m.isLoginUserWrite(),m.getVisitedDate()
//        ));
//    }



    // TODO : 조치 필요
    public static List<GetUserGroupResponse> getUserGroupResponses(List<UserGroup> userGroups, Long userSeq)
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
