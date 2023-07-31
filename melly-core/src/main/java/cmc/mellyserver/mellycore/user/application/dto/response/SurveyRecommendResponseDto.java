package cmc.mellyserver.mellycore.user.application.dto.response;

import cmc.mellyserver.mellycore.place.domain.Position;
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
