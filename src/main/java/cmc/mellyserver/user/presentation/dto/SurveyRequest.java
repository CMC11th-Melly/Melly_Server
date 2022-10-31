package cmc.mellyserver.user.presentation.dto;

import cmc.mellyserver.user.domain.enums.RecommendActivity;
import cmc.mellyserver.user.domain.enums.RecommendGroup;
import cmc.mellyserver.user.domain.enums.RecommendPlace;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SurveyRequest {
    private RecommendGroup recommendGroup;
    private RecommendPlace recommendPlace;
    private RecommendActivity recommendActivity;
}
