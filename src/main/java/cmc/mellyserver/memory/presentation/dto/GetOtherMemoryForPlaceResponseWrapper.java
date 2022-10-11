package cmc.mellyserver.memory.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetOtherMemoryForPlaceResponseWrapper {
    private Long memoryCount;
    private List<GetOtherMemoryForPlaceResponse> memoryList;
}
