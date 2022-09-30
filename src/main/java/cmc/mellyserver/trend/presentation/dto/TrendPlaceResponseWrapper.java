package cmc.mellyserver.trend.presentation.dto;

import cmc.mellyserver.trend.application.dto.TrendResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TrendPlaceResponseWrapper {
    private List<TrendResponseDto> trend;
}
