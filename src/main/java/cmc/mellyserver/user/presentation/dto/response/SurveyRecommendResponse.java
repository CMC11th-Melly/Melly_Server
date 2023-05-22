package cmc.mellyserver.user.presentation.dto.response;

import cmc.mellyserver.place.domain.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

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
