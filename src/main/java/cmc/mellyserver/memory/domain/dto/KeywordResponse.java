package cmc.mellyserver.memory.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * KeywordResponse.java
 *
 * @author jemlog
 */

@Data
@NoArgsConstructor
public class KeywordResponse {
    private Long memoryId;
    private String keyword;
}
