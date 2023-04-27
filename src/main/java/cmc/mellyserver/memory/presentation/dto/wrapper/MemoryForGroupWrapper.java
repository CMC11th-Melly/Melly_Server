package cmc.mellyserver.memory.presentation.dto.wrapper;

import cmc.mellyserver.memory.application.dto.response.MemoryForGroupResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Slice;

@Data
@AllArgsConstructor
public class MemoryForGroupWrapper {
    private Slice<MemoryForGroupResponse> groupMemory;
}
