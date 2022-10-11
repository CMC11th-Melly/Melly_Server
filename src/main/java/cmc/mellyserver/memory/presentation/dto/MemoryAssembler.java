package cmc.mellyserver.memory.presentation.dto;

import cmc.mellyserver.memory.domain.Memory;

import java.util.List;
import java.util.stream.Collectors;

public class MemoryAssembler {

    public static List<GetMemoryForPlaceResponse> getMemoryForPlaceResponse(List<Memory> memories)
    {
       // TODO : 그룹 관리에 대해서 다시 한번 생각히보기
       return memories.stream().map(m -> new GetMemoryForPlaceResponse(m.getId(),m.getMemoryImages().stream().map(mi -> mi.getImagePath()).collect(Collectors.toList()),
                m.getTitle(),m.getContent(),m.getGroupInfo().getGroupType(),m.getGroupInfo().getGroupName(),m.getStars(),m.getKeyword(),m.getCreatedDate())).collect(Collectors.toList());
    }

    public static List<GetOtherMemoryForPlaceResponse> getOtherMemoryForPlaceResponses(List<Memory> memories)
    {
        return memories.stream().map(m -> new GetOtherMemoryForPlaceResponse(m.getId(),m.getMemoryImages().stream().map(mi -> mi.getImagePath()).collect(Collectors.toList()),
                m.getTitle(),m.getContent(),m.getStars(),m.getKeyword(),m.getCreatedDate())).collect(Collectors.toList());
    }
}
