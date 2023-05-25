package cmc.mellyserver.user.application.dto;

import cmc.mellyserver.place.domain.Position;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SurveyRecommendResponseDto {
    private Position position;
    private List<String> words;
}
