package cmc.mellyserver.mellycore.memory.domain.repository.dto;

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
