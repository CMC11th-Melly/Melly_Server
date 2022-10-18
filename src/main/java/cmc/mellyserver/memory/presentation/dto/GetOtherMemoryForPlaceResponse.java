package cmc.mellyserver.memory.presentation.dto;


import cmc.mellyserver.group.domain.enums.GroupType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class GetOtherMemoryForPlaceResponse {

    @Schema(example = "1")
    private Long memoryId;
    @Schema(example = "[melly.jpg,cmc.png]")
    private List<String> memoryImages;
    @Schema(example = "오랜만에 고향 친구랑!")
    private String title;
    @Schema(example = "다음에 친구들 데리고 다시 와야지!")
    private String content;
    @Schema(example = "4.5")
    private Long stars;
    @Schema(example = "[최고에요,맛있어요]")
    private String keyword;
    @Schema(example = "20221014")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyyMMdd")
    private LocalDateTime visitedDate;
}
