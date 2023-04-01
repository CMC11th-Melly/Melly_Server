package cmc.mellyserver.place.placeScrap.presentation;

import cmc.mellyserver.common.response.CommonResponse;
import cmc.mellyserver.place.placeScrap.application.PlaceScrapService;
import cmc.mellyserver.place.placeScrap.presentation.dto.ScrapCancelRequest;
import cmc.mellyserver.place.placeScrap.presentation.dto.ScrapRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PlaceScrapController {

    private final PlaceScrapService scrapService;

    @Operation(summary = "장소 스크랩")
    @PostMapping("/place/scrap")
    public ResponseEntity<CommonResponse> scrapPlace(@AuthenticationPrincipal User user, @RequestBody ScrapRequest scrapRequest)
    {
        scrapService.createScrap(user.getUsername(), scrapRequest);
        return ResponseEntity.ok(new CommonResponse(200,"스크랩 완료"));

    }


    @Operation(summary = "유저가 스크랩한 장소 취소")
    @DeleteMapping("/place/scrap")
    public ResponseEntity<CommonResponse> removeScrap(@AuthenticationPrincipal User user,@RequestBody ScrapCancelRequest scrapCancelRequest){
        scrapService.removeScrap(user.getUsername(), scrapCancelRequest.getLat(),scrapCancelRequest.getLng());
        return ResponseEntity.ok(new CommonResponse(200,"스크랩 삭제 완료"));
        }
}
