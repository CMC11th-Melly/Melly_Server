package cmc.mellyserver.memory.application.dto;

import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.memory.application.dto.response.GroupListForSaveMemoryResponseDto;
import cmc.mellyserver.memory.application.dto.response.MemoryForGroupResponse;
import cmc.mellyserver.memory.application.dto.response.MemoryImageDto;
import cmc.mellyserver.memory.application.dto.response.MemoryUpdateFormResponse;
import cmc.mellyserver.memory.domain.Memory;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * MemoryAssembler.java
 *
 * @author jemlog
 */
public class MemoryAssembler {

    public static MemoryUpdateFormResponse memoryUpdateFormResponse(Memory memory, List<UserGroup> userGroups)
    {
        return new MemoryUpdateFormResponse(
                memory.getMemoryImages().stream().map(mi -> new MemoryImageDto(mi.getId(), mi.getImagePath())).collect(Collectors.toList()),
                memory.getTitle(),
                memory.getContent(),
                userGroups.stream().map(ug -> new GroupListForSaveMemoryResponseDto(ug.getId(), ug.getGroupName(), ug.getGroupType())).collect(Collectors.toList()),
                memory.getStars(),
                memory.getKeyword()
        );
    }
}
