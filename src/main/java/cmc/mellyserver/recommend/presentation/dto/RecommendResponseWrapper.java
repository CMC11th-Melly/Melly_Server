package cmc.mellyserver.recommend.presentation.dto;

import cmc.mellyserver.recommend.application.dto.RecommendResponseDto;
import cmc.mellyserver.trend.application.dto.TrendResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RecommendResponseWrapper {
    private List<RecommendResponseDto> recommend;
}
