package cmc.mellyserver.user.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Slice;

import java.util.List;

@Data
@AllArgsConstructor
public class GetUserMemoryResponseWrapper {
    private Slice<GetUserMemoryResponse> memoryInfo;
}
