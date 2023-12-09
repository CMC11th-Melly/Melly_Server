package cmc.mellyserver.controller.scrap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cmc.mellyserver.auth.common.resolver.CurrentUser;
import cmc.mellyserver.auth.common.resolver.LoginUser;
import cmc.mellyserver.controller.scrap.dto.request.ScrapCancelRequest;
import cmc.mellyserver.controller.scrap.dto.request.ScrapCreateRequest;
import cmc.mellyserver.dbcore.place.Position;
import cmc.mellyserver.domain.scrap.PlaceScrapService;
import cmc.mellyserver.support.response.ApiResponse;
import cmc.mellyserver.support.response.SuccessCode;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/scraps")
public class PlaceScrapController {

    private final PlaceScrapService scrapService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> scrapPlace(@CurrentUser LoginUser user,
        @RequestBody ScrapCreateRequest scrapRequest) {

        scrapService.createScrap(user.getId(), scrapRequest.toServiceRequest());
        return ApiResponse.success(SuccessCode.INSERT_SUCCESS);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> removeScrap(@CurrentUser LoginUser user,
        @RequestBody ScrapCancelRequest scrapCancelRequest) {

        scrapService.removeScrap(user.getId(), new Position(scrapCancelRequest.lat(), scrapCancelRequest.lng()));
        return ApiResponse.success(SuccessCode.DELETE_SUCCESS);
    }

}
