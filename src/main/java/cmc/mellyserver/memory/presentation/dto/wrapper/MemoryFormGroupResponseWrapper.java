package cmc.mellyserver.memory.presentation.dto.wrapper;

import cmc.mellyserver.memory.application.dto.MemoryFormGroupResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MemoryFormGroupResponseWrapper {
    private List<MemoryFormGroupResponse> groupForMemoryForm;
}
