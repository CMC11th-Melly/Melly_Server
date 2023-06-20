package cmc.mellyserver.mellyappexternalapi.memory.presentation.dto.common.wrapper;

import org.springframework.data.domain.Slice;

import cmc.mellyserver.mellyappexternalapi.memory.presentation.dto.response.MemoryResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetMemoryForPlaceResponseWrapper {
	private Long memoryCount;
	private Slice<MemoryResponse> memoryList;
}
