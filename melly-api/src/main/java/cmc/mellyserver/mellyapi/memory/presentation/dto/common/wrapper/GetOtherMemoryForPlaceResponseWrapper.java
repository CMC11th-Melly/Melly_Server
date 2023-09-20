package cmc.mellyserver.mellyapi.memory.presentation.dto.common.wrapper;

import org.springframework.data.domain.Slice;

import cmc.mellyserver.mellyapi.memory.presentation.dto.response.MemoryResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetOtherMemoryForPlaceResponseWrapper {
	private Long memoryCount;
	private Slice<MemoryResponse> memoryList;
}
