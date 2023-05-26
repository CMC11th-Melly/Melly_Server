package cmc.mellyserver.user.presentation.dto.request;

import cmc.mellyserver.common.enums.RecommendGroup;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SurveyRequest {

    private RecommendGroup recommendGroup;
    private String recommendPlace;
    private String recommendActivity;
}
