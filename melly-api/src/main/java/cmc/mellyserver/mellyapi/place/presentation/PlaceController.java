package cmc.mellyserver.mellyapi.place.presentation;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cmc.mellyserver.mellyapi.common.response.CommonResponse;
import cmc.mellyserver.mellyapi.place.application.PlaceService;
import cmc.mellyserver.mellyapi.place.application.dto.MarkedPlaceResponseDto;
import cmc.mellyserver.mellyapi.place.application.dto.PlaceResponseDto;
import cmc.mellyserver.mellyapi.place.presentation.dto.PlaceAssembler;
import cmc.mellyserver.mellyapi.place.presentation.dto.request.PlaceSimpleRequest;
import cmc.mellyserver.mellycore.common.constants.MessageConstant;
import cmc.mellyserver.mellycore.common.enums.GroupType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PlaceController {

	private final PlaceService placeService;

	@GetMapping("/place/list")
	public ResponseEntity<CommonResponse> getPlaceList(@AuthenticationPrincipal User user,
		@RequestParam(value = "groupType") GroupType groupType) {
		List<MarkedPlaceResponseDto> placeReponseDtos = placeService.displayMarkedPlace(
			Long.parseLong(user.getUsername()), groupType);
		return ResponseEntity.ok(new CommonResponse<>(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,
			PlaceAssembler.markedPlaceReponse(placeReponseDtos)));
	}

	@GetMapping("/place/{placeId}/search")
	public ResponseEntity<CommonResponse> getPlaceSearchByMemory(@AuthenticationPrincipal User user,
		@PathVariable Long placeId) {
		PlaceResponseDto placeResponseDto = placeService.findPlaceByPlaceId(Long.parseLong(user.getUsername()),
			placeId);
		return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,
			PlaceAssembler.placeResponse(placeResponseDto)));
	}

	@GetMapping("/place")
	public ResponseEntity<CommonResponse> getDetailPlace(@AuthenticationPrincipal User user,
		PlaceSimpleRequest placeSimpleRequest) {
		PlaceResponseDto placeByPosition = placeService.findPlaceByPosition(Long.parseLong(user.getUsername()),
			placeSimpleRequest.getLat(), placeSimpleRequest.getLng());
		return ResponseEntity.ok(new CommonResponse<>(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,
			PlaceAssembler.placeResponse(placeByPosition)));
	}
}
