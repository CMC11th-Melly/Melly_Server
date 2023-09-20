package cmc.mellyserver.mellyapi.scrap.presentation;

import cmc.mellyserver.mellyapi.common.code.SuccessCode;
import cmc.mellyserver.mellyapi.common.response.ApiResponse;
import cmc.mellyserver.mellyapi.scrap.presentation.dto.ScrapAssembler;
import cmc.mellyserver.mellyapi.scrap.presentation.dto.request.ScrapCancelRequest;
import cmc.mellyserver.mellyapi.scrap.presentation.dto.request.ScrapRequest;
import cmc.mellyserver.mellycore.scrap.application.PlaceScrapService;
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

    @PostMapping("/place/scrap")
    public ResponseEntity<ApiResponse<Void>> scrapPlace(@AuthenticationPrincipal User user, @RequestBody ScrapRequest scrapRequest) {

        scrapService.createScrap(ScrapAssembler.createPlaceScrapRequestDto(Long.parseLong(user.getUsername()), scrapRequest));
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS);
    }

    @DeleteMapping("/place/scrap")
    public ResponseEntity<ApiResponse<Void>> removeScrap(@AuthenticationPrincipal User user, @RequestBody ScrapCancelRequest scrapCancelRequest) {

        scrapService.removeScrap(Long.parseLong(user.getUsername()), scrapCancelRequest.getLat(), scrapCancelRequest.getLng());
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS);
    }
}
