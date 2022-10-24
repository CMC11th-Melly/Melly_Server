package cmc.mellyserver.user.presentation.dto;

import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.memory.domain.Memory;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

public class UserAssembler {

    public static Slice<GetUserMemoryResponse> getUserMemoryResponses(Slice<Memory> memories)
    {
        return memories.map(m -> new GetUserMemoryResponse(m.getPlace().getId(),m.getPlace().getPlaceName(),m.getId(),m.getMemoryImages().stream().map(mi -> mi.getImagePath()).collect(Collectors.toList()),
                m.getTitle(),m.getContent(),m.getGroupInfo().getGroupType(),m.getGroupInfo().getGroupName(),m.getStars(),m.getKeyword(),m.getVisitedDate()
                ));
    }

    public static List<GetUserGroupResponse> getUserGroupResponses(List<UserGroup> userGroups)
    {
        return userGroups.stream().map(ug -> new GetUserGroupResponse(ug.getId(),
                ug.getGroupIcon(),
                ug.getGroupName(),
                ug.getGroupType(),
                ug.getInviteLink(),
                ug.getCreatedDate(),
                ug.getGroupAndUsers().stream().map(gu -> new UserResponseDto(gu.getUser().getUserId(),gu.getUser().getProfileImage(),gu.getUser().getNickname())).collect(Collectors.toList())
                )).collect(Collectors.toList());
    }
}
