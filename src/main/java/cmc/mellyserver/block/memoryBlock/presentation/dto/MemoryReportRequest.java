package cmc.mellyserver.block.memoryBlock.presentation.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemoryReportRequest {

    private Long memoryId;
    private String content;

}
