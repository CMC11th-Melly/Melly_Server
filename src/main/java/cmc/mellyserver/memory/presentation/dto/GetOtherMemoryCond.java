package cmc.mellyserver.memory.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetOtherMemoryCond {

    @Schema(example = "최고에요")
    private String keyword;
    @Schema(example = "20221010")
    private LocalDate visitedDate;
}
