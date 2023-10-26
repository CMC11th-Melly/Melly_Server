package cmc.mellyserver.domain.user.dto.response;

import cmc.mellyserver.dbcore.place.Position;
import lombok.Builder;
import lombok.Data;

import java.util.List;

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
