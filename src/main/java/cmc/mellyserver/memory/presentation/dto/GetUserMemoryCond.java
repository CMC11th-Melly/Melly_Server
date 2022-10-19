package cmc.mellyserver.memory.presentation.dto;

import cmc.mellyserver.group.domain.enums.GroupType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetUserMemoryCond {

    @Schema(example = "최고에요")
    private String keyword;
    @Schema(example = "20221010")
    private String visitedDate;
    @Schema(example = "FRIEND")
    private GroupType groupType;
}
