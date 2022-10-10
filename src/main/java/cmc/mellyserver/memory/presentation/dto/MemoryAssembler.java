package cmc.mellyserver.memory.presentation.dto;

import cmc.mellyserver.memory.domain.Memory;

import java.util.List;
import java.util.stream.Collectors;

public class MemoryAssembler {

    public static List<GetMemoryForPlaceResponse> getMemoryForPlaceResponse(List<Memory> memories)
    {
       // TODO : 그룹 관리에 대해서 다시 한번 생각히보기
       return memories.stream().map(m -> new GetMemoryForPlaceResponse(m.getId(),m.getMemoryImages().stream().map(mi -> mi.getImagePath()).collect(Collectors.toList()),
                m.getTitle(),m.getContent(),m.getGroupInfo().getGroupType(),"떡잎마을 방범대",m.getStars(),m.getKeyword(),"10월 9일")).collect(Collectors.toList());
    }
}
