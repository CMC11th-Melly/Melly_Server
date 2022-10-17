package cmc.mellyserver.memory.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Slice;

import java.util.List;

@Data
@AllArgsConstructor
public class GetMemoryForPlaceResponseWrapper {
    private Long memoryCount;
    private Slice<GetMemoryForPlaceResponse> memoryList;
}
