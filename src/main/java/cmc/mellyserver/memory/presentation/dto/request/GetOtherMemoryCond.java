package cmc.mellyserver.memory.presentation.dto.request;

import cmc.mellyserver.common.enums.GroupType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetOtherMemoryCond {


    @Schema(example = "FRIEND")
    private GroupType groupType;
    @Schema(example = "최고에요")
    private String keyword;
    @Schema(example = "20221010")
    private String visitedDate;
}
