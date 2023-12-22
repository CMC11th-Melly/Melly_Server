package cmc.mellyserver.controller.place;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cmc.mellyserver.auth.common.resolver.CurrentUser;
import cmc.mellyserver.auth.common.resolver.LoginUser;
import cmc.mellyserver.controller.memory.dto.MemoryAssembler;
import cmc.mellyserver.controller.memory.dto.response.FindPlaceInfoByMemoryNameResponse;
import cmc.mellyserver.controller.place.dto.request.PlacePositionRequest;
import cmc.mellyserver.controller.place.dto.response.PlaceResponse;
import cmc.mellyserver.dbcore.group.GroupType;
import cmc.mellyserver.dbcore.place.Position;
import cmc.mellyserver.domain.memory.query.dto.FindPlaceByMemoryTitleResponseDto;
import cmc.mellyserver.domain.place.PlaceService;
import cmc.mellyserver.domain.scrap.dto.MarkedPlaceResponseDto;
import cmc.mellyserver.domain.scrap.dto.PlaceResponseDto;
import cmc.mellyserver.support.response.ApiResponse;
import cmc.mellyserver.support.response.SuccessCode;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/places")
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MarkedPlaceResponseDto>>> getPlaces(@CurrentUser LoginUser user,
        @RequestParam(required = false) GroupType groupType) {

        List<MarkedPlaceResponseDto> placeResponseDtos = placeService.displayMarkedPlace(user.getId(), groupType);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, placeResponseDtos);
    }

    @GetMapping("/{placeId}/search")
    public ResponseEntity<ApiResponse<PlaceResponse>> searchPlaceByPlaceId(@CurrentUser LoginUser user,
        @PathVariable Long placeId) {

        PlaceResponseDto placeResponseDto = placeService.findByPlaceId(user.getId(), placeId);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, PlaceResponse.of(placeResponseDto));
    }

    @GetMapping("/places")
    public ResponseEntity<ApiResponse<PlaceResponse>> getDetailPlace(@AuthenticationPrincipal User user,
        PlacePositionRequest placePositionRequest) {

        PlaceResponseDto placeResponseDto = placeService.findByPosition(Long.parseLong(user.getUsername()),
            new Position(placePositionRequest.lat(), placePositionRequest.lng()));
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, PlaceResponse.of(placeResponseDto));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<FindPlaceInfoByMemoryNameResponse>>> searchPlaceByMemoryTitle(
        @AuthenticationPrincipal User user, @RequestParam String memoryName) {

        List<FindPlaceByMemoryTitleResponseDto> findPlaceInfoByMemoryNameResponseDtos = placeService
            .findByMemoryTitle(Long.parseLong(user.getUsername()), memoryName);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS,
            MemoryAssembler.findPlaceInfoByMemoryNameResponses(findPlaceInfoByMemoryNameResponseDtos));
    }

}
