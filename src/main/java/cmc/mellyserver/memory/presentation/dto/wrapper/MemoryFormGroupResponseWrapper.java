package cmc.mellyserver.memory.presentation.dto.wrapper;

import cmc.mellyserver.memory.application.dto.response.GroupListForSaveMemoryResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MemoryFormGroupResponseWrapper {
    private List<GroupListForSaveMemoryResponseDto> groupForMemoryForm;
}
