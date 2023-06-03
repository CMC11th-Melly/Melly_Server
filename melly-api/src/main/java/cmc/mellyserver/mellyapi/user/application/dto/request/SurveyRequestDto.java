package cmc.mellyserver.mellyapi.user.application.dto.request;

import cmc.mellyserver.mellycore.common.enums.RecommendActivity;
import cmc.mellyserver.mellycore.common.enums.RecommendGroup;
import cmc.mellyserver.mellycore.common.enums.RecommendPlace;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SurveyRequestDto {

	private Long userSeq;

	private RecommendGroup recommendGroup;

	private RecommendPlace recommendPlace;

	private RecommendActivity recommendActivity;

	@Builder
	public SurveyRequestDto(Long userSeq, RecommendGroup recommendGroup, RecommendPlace recommendPlace,
		RecommendActivity recommendActivity) {
		this.userSeq = userSeq;
		this.recommendGroup = recommendGroup;
		this.recommendPlace = recommendPlace;
		this.recommendActivity = recommendActivity;
	}

}
