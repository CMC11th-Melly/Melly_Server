package cmc.mellyserver.controller.memory.dto.common.wrapper;

import cmc.mellyserver.controller.memory.dto.response.MemoryResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Slice;

@Data
@AllArgsConstructor
public class GetMyGroupMemoryForPlaceResponseWrapper {

	private Long memoryCount;

	private Slice<MemoryResponse> memoryList;

}
