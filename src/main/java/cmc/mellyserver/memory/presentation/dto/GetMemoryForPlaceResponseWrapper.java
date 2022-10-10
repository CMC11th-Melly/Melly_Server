package cmc.mellyserver.memory.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetMemoryForPlaceResponseWrapper {
    private List<GetMemoryForPlaceResponse> memoryList;
}
