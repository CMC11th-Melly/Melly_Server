package cmc.mellyserver.mellyappexternalapi.user.presentation.dto.response;

import cmc.mellyserver.mellydomain.place.domain.Position;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
public class SurveyRecommendResponse {

    private Position position;
    private List<String> words;

    @Builder
    public SurveyRecommendResponse(Position position, List<String> words) {
        this.position = position;
        this.words = words;
    }
}
