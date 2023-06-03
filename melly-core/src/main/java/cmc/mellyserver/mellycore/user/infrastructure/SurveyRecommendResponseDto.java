package cmc.mellyserver.mellycore.user.infrastructure;

import java.util.List;

import cmc.mellyserver.mellycore.place.domain.Position;
import lombok.Builder;
import lombok.Data;

@Data
public class SurveyRecommendResponseDto {
	private Position position;
	private List<String> words;

	@Builder
	public SurveyRecommendResponseDto(Position position, List<String> words) {
		this.position = position;
		this.words = words;
	}
}
