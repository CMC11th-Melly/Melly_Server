package cmc.mellyserver.mellycore.memory.domain.repository.dto;

import cmc.mellyserver.mellycore.group.domain.enums.GroupType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
public class MemoryResponseDto {


    private Long memoryId;

    private String imagePath;

    private String title;

    private LocalDate visitedDate;

    private GroupType groupType;


    @Builder
    public MemoryResponseDto(Long memoryId, String title, String imagePath, GroupType groupType, LocalDate visitedDate) {
        this.memoryId = memoryId;
        this.title = title;
        this.imagePath = imagePath;
        this.groupType = groupType;
        this.visitedDate = visitedDate;
    }

    public MemoryResponseDto(Long memoryId, String title, String imagePath, LocalDate visitedDate, GroupType groupType) {

        this.memoryId = memoryId;
        this.title = title;
        this.imagePath = imagePath;
        this.visitedDate = visitedDate;
        this.groupType = groupType;

    }
}
