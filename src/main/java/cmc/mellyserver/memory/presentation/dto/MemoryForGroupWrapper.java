package cmc.mellyserver.memory.presentation.dto;

import cmc.mellyserver.memory.application.dto.MemoryForGroupResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Slice;

import java.util.List;

@Data
@AllArgsConstructor
public class MemoryForGroupWrapper {
    private Slice<MemoryForGroupResponse> groupMemory;
}
