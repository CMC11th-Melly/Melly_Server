package cmc.mellyserver.memory.presentation.dto.wrapper;

import cmc.mellyserver.memory.presentation.dto.request.SearchMemoryByNameResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchMemoryNameResponseWrapper {
  private List<SearchMemoryByNameResponseDto> memoryNames;
}
