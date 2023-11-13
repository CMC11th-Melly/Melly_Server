package cmc.mellyserver.domain.user.dto.response;

import java.util.List;

import cmc.mellyserver.dbcore.place.Position;
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
