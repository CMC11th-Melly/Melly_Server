package cmc.mellyserver.memory.presentation.dto.wrapper;

import cmc.mellyserver.memory.application.dto.response.MemoryUpdateFormResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemoryUpDateFormResponseWrapper {
    private MemoryUpdateFormResponseDto memoryUpdateForm;
}
