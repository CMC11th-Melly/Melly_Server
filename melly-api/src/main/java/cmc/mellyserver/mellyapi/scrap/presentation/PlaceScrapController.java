package cmc.mellyserver.mellyapi.scrap.presentation;

import cmc.mellyserver.mellyapi.scrap.presentation.dto.ScrapAssembler;
import cmc.mellyserver.mellyapi.scrap.presentation.dto.request.ScrapCancelRequest;
import cmc.mellyserver.mellyapi.scrap.presentation.dto.request.ScrapRequest;
import cmc.mellyserver.mellycore.scrap.application.PlaceScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import static cmc.mellyserver.mellyapi.common.constants.ResponseConstants.CREATED;
import static cmc.mellyserver.mellyapi.common.constants.ResponseConstants.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PlaceScrapController {

    private final PlaceScrapService scrapService;

    @PostMapping("/place/scrap")
    public ResponseEntity<Void> scrapPlace(@AuthenticationPrincipal User user,
                                           @RequestBody ScrapRequest scrapRequest) {

        scrapService.createScrap(ScrapAssembler.createPlaceScrapRequestDto(Long.parseLong(user.getUsername()), scrapRequest));
        return CREATED;
    }

    @DeleteMapping("/place/scrap")
    public ResponseEntity<Void> removeScrap(@AuthenticationPrincipal User user, @RequestBody ScrapCancelRequest scrapCancelRequest) {

        scrapService.removeScrap(Long.parseLong(user.getUsername()), scrapCancelRequest.getLat(), scrapCancelRequest.getLng());
        return NO_CONTENT;
    }
}
