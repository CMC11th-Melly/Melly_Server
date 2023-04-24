package cmc.mellyserver.memory.presentation.dto.wrapper;

import cmc.mellyserver.memory.domain.dto.UserCreatedMemoryListResponseDto;
import cmc.mellyserver.memory.presentation.dto.response.GetOtherMemoryForPlaceResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Slice;

@Data
@AllArgsConstructor
public class GetOtherMemoryForPlaceResponseWrapper {
    private Long memoryCount;
    private Slice<UserCreatedMemoryListResponseDto> memoryList;
}
