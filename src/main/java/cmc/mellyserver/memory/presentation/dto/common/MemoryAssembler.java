package cmc.mellyserver.memory.presentation.dto.common;

import cmc.mellyserver.memory.application.dto.MemoryForGroupResponse;
import cmc.mellyserver.memory.application.dto.MemoryFormGroupResponse;
import cmc.mellyserver.memory.application.dto.MemoryImageDto;
import cmc.mellyserver.memory.application.dto.MemoryUpdateFormResponse;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.presentation.dto.response.GetMemoryForPlaceResponse;
import cmc.mellyserver.memory.presentation.dto.response.GetOtherMemoryForPlaceResponse;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

public class MemoryAssembler {


    public static Slice<GetMemoryForPlaceResponse> getMemoryForPlaceResponse(Slice<Memory> memories)
    {

       return memories.map(m -> new GetMemoryForPlaceResponse(m.getPlace().getId(),m.getPlace().getPlaceName(),m.getId(),m.getMemoryImages().stream().map(mi -> new ImageDto(mi.getId(),mi.getImagePath())).collect(Collectors.toList()),
                m.getTitle(),m.getContent(),m.getGroupInfo().getGroupType(),m.getGroupInfo().getGroupName(),m.getStars(),m.getKeyword(),m.getVisitedDate()));
    }



    public static Slice<GetOtherMemoryForPlaceResponse> getOtherMemoryForPlaceResponses(Slice<Memory> memories)
    {
        return memories.map(m -> new GetOtherMemoryForPlaceResponse(m.getPlace().getId(),m.getPlace().getPlaceName(),m.getId(),m.getMemoryImages().stream().map(mi -> new ImageDto(mi.getId(),mi.getImagePath())).collect(Collectors.toList()),
                m.getTitle(),m.getContent(),m.getGroupInfo().getGroupType(),m.getGroupInfo().getGroupName(),m.getStars(),m.getKeyword(),m.getVisitedDate()));
    }



    public static MemoryUpdateFormResponse memoryUpdateFormResponse(Memory memory, List<MemoryFormGroupResponse> memoryFormGroupResponse)
    {
        return new MemoryUpdateFormResponse(memory.getMemoryImages().stream().map(m -> new MemoryImageDto(m.getId(),m.getImagePath())).collect(Collectors.toList()),
                memory.getTitle(),
                memory.getContent(),
                memoryFormGroupResponse,memory.getStars(),
                memory.getKeyword());
    }


    public static Slice<MemoryForGroupResponse> memoryForGroupResponseSlice(Slice<Memory> myGroupMemory)
    {
        return myGroupMemory
                .map(memory -> new MemoryForGroupResponse(memory.getPlace().getId(),
                        memory.getPlace().getPlaceName(),
                        memory.getId(),
                        memory.getMemoryImages().stream().map(mi -> new ImageDto(mi.getId(), mi.getImagePath()))
                                .collect(Collectors.toList()),
                        memory.getTitle(),
                        memory.getContent(),
                        memory.getGroupInfo().getGroupType(),
                        memory.getGroupInfo().getGroupName(),
                        memory.getStars(),
                        memory.getKeyword(),
                        memory.getVisitedDate()));
    }
}
