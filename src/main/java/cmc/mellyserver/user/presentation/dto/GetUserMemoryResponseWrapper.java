package cmc.mellyserver.user.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetUserMemoryResponseWrapper {
    private List<GetUserMemoryResponse> memoryInfo;
}
