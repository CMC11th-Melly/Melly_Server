package cmc.mellyserver.user.presentation.dto.request;

import cmc.mellyserver.user.domain.enums.RecommendActivity;
import cmc.mellyserver.user.domain.enums.RecommendGroup;
import cmc.mellyserver.user.domain.enums.RecommendPlace;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SurveyRequest {

    @Schema(description = "FRIEND, FAMILY, COUPLE, COMPANY 중 하나 입력",example = "FRIEND")
    private RecommendGroup recommendGroup;
    private String recommendPlace;
    private String recommendActivity;
}
