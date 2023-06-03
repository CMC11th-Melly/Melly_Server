package cmc.mellyserver.mellyapi.scrap.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cmc.mellyserver.mellyapi.common.response.CommonResponse;
import cmc.mellyserver.mellyapi.scrap.application.PlaceScrapService;
import cmc.mellyserver.mellyapi.scrap.presentation.dto.request.ScrapAssembler;
import cmc.mellyserver.mellyapi.scrap.presentation.dto.request.ScrapCancelRequest;
import cmc.mellyserver.mellyapi.scrap.presentation.dto.request.ScrapRequest;
import cmc.mellyserver.mellycore.common.constants.MessageConstant;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PlaceScrapController {

	private final PlaceScrapService scrapService;

	@PostMapping("/place/scrap")
	public ResponseEntity<CommonResponse> scrapPlace(@AuthenticationPrincipal User user,
		@RequestBody ScrapRequest scrapRequest) {
		scrapService.createScrap(
			ScrapAssembler.createPlaceScrapRequestDto(Long.parseLong(user.getUsername()), scrapRequest));
		return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
	}

	@DeleteMapping("/place/scrap")
	public ResponseEntity<CommonResponse> removeScrap(@AuthenticationPrincipal User user,
		@RequestBody ScrapCancelRequest scrapCancelRequest) {
		scrapService.removeScrap(Long.parseLong(user.getUsername()), scrapCancelRequest.getLat(),
			scrapCancelRequest.getLng());
		return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS));
	}
}
