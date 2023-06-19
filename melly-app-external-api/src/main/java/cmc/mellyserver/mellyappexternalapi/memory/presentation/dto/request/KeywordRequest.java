package cmc.mellyserver.mellyappexternalapi.memory.presentation.dto.request;

import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class KeywordRequest {
	private List<String> keyword;
}
