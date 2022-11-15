package cmc.mellyserver.memory.presentation.dto.wrapper;

import cmc.mellyserver.memory.presentation.dto.response.GetMemoryForPlaceResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Slice;

@Data
@AllArgsConstructor
public class GetMemoryForPlaceResponseWrapper {
    private Long memoryCount;
    private Slice<GetMemoryForPlaceResponse> memoryList;
}
