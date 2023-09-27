package cmc.mellyserver.mellyapi.domain.memory.query.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * KeywordResponseDto.java
 *
 * @author jemlog
 */

@Data
@NoArgsConstructor
public class KeywordResponseDto {
    private Long memoryId;
    private String keyword;
}
