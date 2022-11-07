package cmc.mellyserver.user.presentation.dto.common;

import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.presentation.dto.common.ImageDto;
import cmc.mellyserver.user.domain.User;
import cmc.mellyserver.user.presentation.dto.response.GetUserGroupResponse;
import cmc.mellyserver.user.presentation.dto.response.GetUserMemoryResponse;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

public class UserAssembler {


    public static Slice<GetUserMemoryResponse> getUserMemoryResponses(Slice<Memory> memories, User user)
    {
        return memories.map(m -> new GetUserMemoryResponse(m.getPlace().getId(),m.getPlace().getPlaceName(),m.getId(),m.getMemoryImages().stream().map(mi -> new ImageDto(mi.getId(),mi.getImagePath())).collect(Collectors.toList()),
                m.getTitle(),m.getContent(),m.getGroupInfo().getGroupType(),m.getGroupInfo().getGroupName(),m.getStars(),m.getKeyword(),user.getMemories().stream().anyMatch((um -> um.getId().equals(m.getId()))),m.getVisitedDate()
                ));
    }

    public static List<GetUserGroupResponse> getUserGroupResponses(List<UserGroup> userGroups, String userId)
    {
        return userGroups.stream().map(ug -> new GetUserGroupResponse(ug.getId(),
                ug.getGroupIcon(),
                ug.getGroupName(),
                ug.getGroupAndUsers().stream().map(gu -> new UserDto(
                        gu.getUser().getUserSeq(),
                        gu.getUser().getProfileImage(),
                        gu.getUser().getNickname(),
                        (gu.getUser().getUserId().equals(userId)) ? true : false
                )).collect(Collectors.toList()),
                ug.getGroupType(),
                ug.getInviteLink()
                )).collect(Collectors.toList());
    }
}
