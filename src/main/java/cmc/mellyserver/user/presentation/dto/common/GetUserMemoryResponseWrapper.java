package cmc.mellyserver.user.presentation.dto.common;

import cmc.mellyserver.user.presentation.dto.response.GetUserMemoryResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Slice;

@Data
@AllArgsConstructor
public class GetUserMemoryResponseWrapper {
    private Slice<GetUserMemoryResponse> memoryInfo;
}
