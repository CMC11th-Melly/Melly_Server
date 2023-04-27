package cmc.mellyserver.user.presentation.dto.common;


import cmc.mellyserver.memory.domain.dto.MemoryResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Slice;


@Data
@AllArgsConstructor
public class GetUserMemoryResponseWrapper {
    private Slice<MemoryResponseDto> memoryInfo;
}
