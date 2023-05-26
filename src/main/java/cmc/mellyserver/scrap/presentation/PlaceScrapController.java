package cmc.mellyserver.scrap.presentation;

import cmc.mellyserver.common.constants.MessageConstant;
import cmc.mellyserver.common.response.CommonResponse;
import cmc.mellyserver.scrap.application.PlaceScrapService;
import cmc.mellyserver.scrap.application.dto.request.CreatePlaceScrapRequestDto;
import cmc.mellyserver.scrap.presentation.dto.request.ScrapCancelRequest;
import cmc.mellyserver.scrap.presentation.dto.request.ScrapRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<CommonResponse> scrapPlace(@AuthenticationPrincipal User user, @RequestBody ScrapRequest scrapRequest) {
        scrapService.createScrap(CreatePlaceScrapRequestDto.of(Long.parseLong(user.getUsername()), scrapRequest));
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }


    @DeleteMapping("/place/scrap")
    public ResponseEntity<CommonResponse> removeScrap(@AuthenticationPrincipal User user,@RequestBody ScrapCancelRequest scrapCancelRequest){
        scrapService.removeScrap(Long.parseLong(user.getUsername()), scrapCancelRequest.getLat(),scrapCancelRequest.getLng());
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
    }
}
