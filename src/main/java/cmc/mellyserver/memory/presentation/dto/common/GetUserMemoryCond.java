package cmc.mellyserver.memory.presentation.dto.common;

import cmc.mellyserver.common.enums.GroupType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetUserMemoryCond {

    @Schema(example = "FRIEND")
    private GroupType groupType;
}
