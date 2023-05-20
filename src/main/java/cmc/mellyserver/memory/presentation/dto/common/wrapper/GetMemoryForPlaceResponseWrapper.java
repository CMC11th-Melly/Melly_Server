package cmc.mellyserver.memory.presentation.dto.common.wrapper;

import cmc.mellyserver.memory.presentation.dto.response.MemoryResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Slice;

@Data
@AllArgsConstructor
public class GetMemoryForPlaceResponseWrapper {
    private Long memoryCount;
    private Slice<MemoryResponse> memoryList;
}
