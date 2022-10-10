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

    @Schema(example = "좋아요")
    private String keyword;
    @Schema(example = "10월 9일")
    private LocalDate createdDate;
    @Schema(example = "FRIEND")
    private GroupType groupType;
}