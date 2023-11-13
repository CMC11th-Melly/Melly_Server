package cmc.mellyserver.domain.memory.query.dto;

import java.time.LocalDate;

import cmc.mellyserver.dbcore.group.GroupType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemoryResponseDto {

    private Long memoryId;

    private String title;

    private LocalDate visitedDate;

    private GroupType groupType;

    @Builder
    public MemoryResponseDto(Long memoryId, String title, GroupType groupType, LocalDate visitedDate) {
        this.memoryId = memoryId;
        this.title = title;
        this.groupType = groupType;
        this.visitedDate = visitedDate;
    }

    public MemoryResponseDto(Long memoryId, String title, LocalDate visitedDate, GroupType groupType) {

        this.memoryId = memoryId;
        this.title = title;
        this.visitedDate = visitedDate;
        this.groupType = groupType;
    }

}
