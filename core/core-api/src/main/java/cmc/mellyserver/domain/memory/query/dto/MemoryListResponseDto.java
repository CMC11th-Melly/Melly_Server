package cmc.mellyserver.domain.memory.query.dto;

import java.time.LocalDate;

import cmc.mellyserver.dbcore.group.GroupType;

public record MemoryListResponseDto(

    Long memoryId,
    String title,
    LocalDate visitedDate,
    GroupType groupType

) {
}
