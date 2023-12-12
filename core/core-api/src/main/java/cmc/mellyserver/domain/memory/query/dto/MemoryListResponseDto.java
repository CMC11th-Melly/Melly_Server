package cmc.mellyserver.domain.memory.query.dto;

import java.time.LocalDate;

public record MemoryListResponseDto(

    Long memoryId,
    String title,
    String imageUrl,
    LocalDate visitedDate
) {
}
