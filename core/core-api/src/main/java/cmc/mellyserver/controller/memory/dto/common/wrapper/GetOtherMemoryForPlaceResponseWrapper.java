package cmc.mellyserver.controller.memory.dto.common.wrapper;

import org.springframework.data.domain.Slice;

import cmc.mellyserver.controller.memory.dto.response.MemoryResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetOtherMemoryForPlaceResponseWrapper {

	private Long memoryCount;

	private Slice<MemoryResponse> memoryList;

}
