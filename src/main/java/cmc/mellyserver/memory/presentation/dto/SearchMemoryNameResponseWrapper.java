package cmc.mellyserver.memory.presentation.dto;

import cmc.mellyserver.memory.domain.MemorySearchDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchMemoryNameResponseWrapper {
  private List<MemorySearchDto> memoryNames;
}
