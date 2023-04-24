package cmc.mellyserver.trend.presentation;

import cmc.mellyserver.common.response.CommonResponse;
import cmc.mellyserver.trend.application.TrendService;
import cmc.mellyserver.trend.application.dto.TrendResponseDto;
import cmc.mellyserver.trend.presentation.dto.TrendPlaceResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TrendController {

    private final TrendService trendService;

    @GetMapping("/trend")
    public  ResponseEntity<CommonResponse<TrendPlaceResponseWrapper>> trendPlace(@AuthenticationPrincipal User user)
    {
        List<TrendResponseDto> trend = trendService.getTrend(Long.parseLong(user.getUsername()));
        return ResponseEntity.ok(new CommonResponse(200,"핫한 장소",new TrendPlaceResponseWrapper(trend)));
    }
}
