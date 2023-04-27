package cmc.mellyserver.memory.presentation.dto.common;

import cmc.mellyserver.memory.application.dto.response.MemoryForGroupResponse;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.user.domain.User;
import org.springframework.data.domain.Slice;

public class MemoryAssembler {


//    public static Slice<GetMemoryForPlaceResponse> getMemoryForPlaceResponse(Slice<Memory> memories, User user)
//    {
//
//       return memories.map(m -> new GetMemoryForPlaceResponse(m.getPlace().getId(),m.getPlace().getPlaceName(),m.getId(),m.getMemoryImages().stream().map(mi -> new ImageDto(mi.getId(),mi.getImagePath())).collect(Collectors.toList()),
//                m.getTitle(),m.getContent(),m.getGroupInfo().getGroupType(),m.getGroupInfo().getGroupName(),m.getStars(),m.getKeyword(),user.getMemories().stream().anyMatch((um -> um.getId().equals(m.getId()))),m.getVisitedDate()));
//    }
//
//
//    public static GetMemoryByMemoryIdResponse getMemoryByMemoryIdResponse(Memory memory, User user)
//    {
//
//        return new GetMemoryByMemoryIdResponse(
//                memory.getPlace().getId(),
//                memory.getPlace().getPlaceName(),
//                memory.getId(),
//                memory.getMemoryImages().stream().map(mi -> new ImageDto(mi.getId(), mi.getImagePath())).collect(Collectors.toList()),
//                memory.getTitle(),
//                memory.getContent(),
//                memory.getGroupInfo().getGroupType(),
//                memory.getGroupInfo().getGroupName(),
//                memory.getStars(),
//                memory.getKeyword(),
//                user.getMemories().stream().anyMatch((um -> um.getId().equals(memory.getId()))),
//                memory.getVisitedDate());
//    }
//
//
//
//    public static Slice<GetOtherMemoryForPlaceResponse> getOtherMemoryForPlaceResponses(Slice<Memory> memories,User user)
//    {
//        return memories.map(m -> new GetOtherMemoryForPlaceResponse(m.getPlace().getId(),m.getPlace().getPlaceName(),m.getId(),m.getMemoryImages().stream().map(mi -> new ImageDto(mi.getId(),mi.getImagePath())).collect(Collectors.toList()),
//                m.getTitle(),m.getContent(),m.getGroupInfo().getGroupType(),m.getGroupInfo().getGroupName(),m.getStars(),m.getKeyword(),user.getMemories().stream().anyMatch((um -> um.getId().equals(m.getId()))),m.getVisitedDate()));
//    }
//
//
//
//    public static MemoryUpdateFormResponse memoryUpdateFormResponse(Memory memory, List<GroupListForSaveMemoryResponseDto> memoryFormGroupResponse)
//    {
//        return new MemoryUpdateFormResponse(memory.getMemoryImages().stream().map(m -> new MemoryImageDto(m.getId(),m.getImagePath())).collect(Collectors.toList()),
//                memory.getTitle(),
//                memory.getContent(),
//                memoryFormGroupResponse,memory.getStars(),
//                memory.getKeyword());
//    }


    public static Slice<MemoryForGroupResponse> memoryForGroupResponseSlice(Slice<Memory> myGroupMemory,User user)
    {
//        return myGroupMemory
//                .map(memory -> new MemoryForGroupResponse(memory.getPlaceId(),
//                        memory.getPlace().getPlaceName(),
//                        memory.getId(),
//                        memory.getMemoryImages().stream().map(mi -> new ImageDto(mi.getId(), mi.getImagePath()))
//                                .collect(Collectors.toList()),
//                        memory.getTitle(),
//                        memory.getContent(),
//                        memory.getGroupInfo().getGroupType(),
//                        memory.getGroupInfo().getGroupName(),
//                        memory.getStars(),
//                        memory.getKeyword(),
//                        user.getMemories().stream().anyMatch((um -> um.getId().equals(memory.getId()))),
//                        memory.getVisitedDate()));
        return null;
    }
}
