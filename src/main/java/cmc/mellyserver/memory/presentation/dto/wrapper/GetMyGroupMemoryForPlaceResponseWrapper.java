package cmc.mellyserver.memory.presentation.dto.wrapper;

import cmc.mellyserver.memory.application.dto.MemoryForGroupResponse;
import cmc.mellyserver.memory.presentation.dto.response.GetOtherMemoryForPlaceResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Slice;

@Data
@AllArgsConstructor
public class GetMyGroupMemoryForPlaceResponseWrapper {
    private Long memoryCount;
    private Slice<MemoryForGroupResponse> memoryList;
}
