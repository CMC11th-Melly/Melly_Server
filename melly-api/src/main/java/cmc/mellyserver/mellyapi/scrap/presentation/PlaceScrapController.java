package cmc.mellyserver.mellyapi.scrap.presentation;

import cmc.mellyserver.mellyapi.common.constants.MessageConstant;
import cmc.mellyserver.mellyapi.common.response.ApiResponse;
import cmc.mellyserver.mellyapi.scrap.presentation.dto.ScrapAssembler;
import cmc.mellyserver.mellyapi.scrap.presentation.dto.request.ScrapCancelRequest;
import cmc.mellyserver.mellyapi.scrap.presentation.dto.request.ScrapRequest;
import cmc.mellyserver.mellycore.scrap.application.PlaceScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PlaceScrapController {

    private final PlaceScrapService scrapService;

    @PostMapping("/place/scrap")
    public ResponseEntity<ApiResponse> scrapPlace(@AuthenticationPrincipal User user,
        @RequestBody ScrapRequest scrapRequest) {

        scrapService.createScrap(
            ScrapAssembler.createPlaceScrapRequestDto(Long.parseLong(user.getUsername()),
                scrapRequest));
        return ResponseEntity.ok(
            new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }

    @DeleteMapping("/place/scrap")
    public ResponseEntity<ApiResponse> removeScrap(@AuthenticationPrincipal User user,
        @RequestBody ScrapCancelRequest scrapCancelRequest) {

        scrapService.removeScrap(Long.parseLong(user.getUsername()), scrapCancelRequest.getLat(),
            scrapCancelRequest.getLng());
        return ResponseEntity.ok(
            new ApiResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }
}
