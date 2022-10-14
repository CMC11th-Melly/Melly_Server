package cmc.mellyserver.memory.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchMemoryNameResponseWrapper {
  private List<MemorySearchDto> memoryNames;
}
