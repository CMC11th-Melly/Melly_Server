package cmc.mellyserver.recommend.presentation;

import cmc.mellyserver.common.response.CommonResponse;
import cmc.mellyserver.recommend.application.RecommendService;
import cmc.mellyserver.recommend.application.dto.RecommendResponseDto;
import cmc.mellyserver.recommend.presentation.dto.RecommendResponseWrapper;
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
public class RecommendController {

    private final RecommendService recommendService;

    @GetMapping("/recommend")
    public  ResponseEntity<CommonResponse<RecommendResponseWrapper>> recommendPlace(@AuthenticationPrincipal User user)
    {
        List<RecommendResponseDto> recommend = recommendService.getRecommend(user.getUsername());
        return ResponseEntity.ok(new CommonResponse(200,"추천 장소",new RecommendResponseWrapper(recommend)));
    }
}
