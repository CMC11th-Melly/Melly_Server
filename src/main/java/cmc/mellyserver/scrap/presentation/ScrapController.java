package cmc.mellyserver.scrap.presentation;

import cmc.mellyserver.common.response.CommonResponse;
import cmc.mellyserver.scrap.application.ScrapService;
import cmc.mellyserver.scrap.application.dto.ScrapedPlaceResponseDto;
import cmc.mellyserver.scrap.presentation.dto.ScrapCancelRequest;
import cmc.mellyserver.scrap.presentation.dto.ScrapRequest;
import cmc.mellyserver.scrap.presentation.dto.ScrapResponseWrapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ScrapController {

    private final ScrapService scrapService;

    @Operation(summary = "장소 스크랩")
    @PostMapping("/place/scrap")
    public ResponseEntity<CommonResponse> scrapPlace(@AuthenticationPrincipal User user, @RequestBody ScrapRequest scrapRequest)
    {
        scrapService.createScrap(user.getUsername(), scrapRequest);
        return ResponseEntity.ok(new CommonResponse(200,"스크랩 완료"));

    }

    @Operation(summary = "유저가 스크랩한 장소 조회")
    @GetMapping("/place/scrap")
    public ResponseEntity<CommonResponse> getUserScrap(@AuthenticationPrincipal User user)
    {
        List<ScrapedPlaceResponseDto> scrapedPlace = scrapService.getScrapedPlace(user.getUsername());
        return ResponseEntity.ok(new CommonResponse(200,"유저가 스크랩한 장소 목록",new ScrapResponseWrapper(scrapedPlace)));
    }

    @Operation(summary = "유저가 스크랩한 장소 취소")
    @DeleteMapping("/place/scrap")
    public ResponseEntity<CommonResponse> removeScrap(@AuthenticationPrincipal User user,@RequestBody ScrapCancelRequest scrapCancelRequest){
        scrapService.removeScrap(user.getUsername(), scrapCancelRequest.getLat(),scrapCancelRequest.getLng());
        return ResponseEntity.ok(new CommonResponse(200,"스크랩 삭제 완료"));
        }
}
