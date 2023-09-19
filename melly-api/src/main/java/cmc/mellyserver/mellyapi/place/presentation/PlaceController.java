package cmc.mellyserver.mellyapi.place.presentation;


import cmc.mellyserver.mellyapi.common.code.SuccessCode;
import cmc.mellyserver.mellyapi.common.response.ApiResponse;
import cmc.mellyserver.mellyapi.memory.presentation.dto.MemoryAssembler;
import cmc.mellyserver.mellyapi.memory.presentation.dto.response.FindPlaceInfoByMemoryNameResponse;
import cmc.mellyserver.mellyapi.place.presentation.dto.PlaceAssembler;
import cmc.mellyserver.mellyapi.place.presentation.dto.request.PlaceSimpleRequest;
import cmc.mellyserver.mellyapi.place.presentation.dto.response.PlaceResponse;
import cmc.mellyserver.mellycore.group.domain.enums.GroupType;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.FindPlaceInfoByMemoryNameResponseDto;
import cmc.mellyserver.mellycore.place.application.PlaceService;
import cmc.mellyserver.mellycore.scrap.application.dto.MarkedPlaceResponseDto;
import cmc.mellyserver.mellycore.scrap.application.dto.PlaceResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping("/place/list")
    public ResponseEntity<ApiResponse<List<MarkedPlaceResponseDto>>> getPlaceList(@AuthenticationPrincipal User user,
                                                                                  @RequestParam(value = "groupType") GroupType groupType) {

        List<MarkedPlaceResponseDto> placeReponseDtos = placeService.displayMarkedPlace(Long.parseLong(user.getUsername()), groupType);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, placeReponseDtos);
    }

    @GetMapping("/place/{placeId}/search")
    public ResponseEntity<ApiResponse<PlaceResponse>> getPlaceSearchByMemory(@AuthenticationPrincipal User user, @PathVariable Long placeId) {

        PlaceResponseDto placeResponseDto = placeService.findPlaceByPlaceId(Long.parseLong(user.getUsername()), placeId);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, PlaceAssembler.placeResponse(placeResponseDto));
    }

    @GetMapping("/place")
    public ResponseEntity<ApiResponse<PlaceResponse>> getDetailPlace(@AuthenticationPrincipal User user, PlaceSimpleRequest placeSimpleRequest) {

        PlaceResponseDto placeByPosition = placeService.findPlaceByPosition(Long.parseLong(user.getUsername()), placeSimpleRequest.getLat(), placeSimpleRequest.getLng());
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, PlaceAssembler.placeResponse(placeByPosition));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<FindPlaceInfoByMemoryNameResponse>>> searchPlaceByMemoryTitle(@AuthenticationPrincipal User user, @RequestParam String memoryName) {

        List<FindPlaceInfoByMemoryNameResponseDto> findPlaceInfoByMemoryNameResponseDtos = placeService.findPlaceInfoByMemoryName(Long.parseLong(user.getUsername()), memoryName);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, MemoryAssembler.findPlaceInfoByMemoryNameResponses(findPlaceInfoByMemoryNameResponseDtos));
    }
}
