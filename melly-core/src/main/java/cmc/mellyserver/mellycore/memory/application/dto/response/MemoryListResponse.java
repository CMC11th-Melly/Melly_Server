package cmc.mellyserver.mellycore.memory.application.dto.response;

import cmc.mellyserver.mellycore.memory.domain.repository.dto.MemoryResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MemoryListResponse {

    private List<MemoryResponseDto> contents;
    private Boolean next;

    public static MemoryListResponse from(List<MemoryResponseDto> contents, Boolean next) {
        return new MemoryListResponse(contents, next);
    }
}
