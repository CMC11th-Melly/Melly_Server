package cmc.mellyserver.controller.user.dto.response;

import java.util.List;

import cmc.mellyserver.dbcore.place.Position;
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
