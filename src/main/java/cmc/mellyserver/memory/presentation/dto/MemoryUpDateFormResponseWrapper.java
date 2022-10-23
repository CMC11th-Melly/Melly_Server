package cmc.mellyserver.memory.presentation.dto;

import cmc.mellyserver.memory.application.dto.MemoryUpdateFormResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemoryUpDateFormResponseWrapper {
    private MemoryUpdateFormResponse memoryUpdateForm;
}
