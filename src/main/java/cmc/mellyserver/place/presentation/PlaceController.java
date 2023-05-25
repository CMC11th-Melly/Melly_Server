package cmc.mellyserver.place.presentation;

import cmc.mellyserver.common.constants.MessageConstant;
import cmc.mellyserver.common.response.CommonDetailResponse;
import cmc.mellyserver.common.response.CommonResponse;
import cmc.mellyserver.common.enums.GroupType;
import cmc.mellyserver.place.application.PlaceService;
import cmc.mellyserver.place.application.dto.PlaceResponseDto;
import cmc.mellyserver.place.presentation.dto.PlaceAssembler;
import cmc.mellyserver.place.presentation.dto.request.PlaceSimpleRequest;
import cmc.mellyserver.place.application.dto.MarkedPlaceReponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    @Operation(summary = "특정 좌표 영역 내의 장소 데이터를 모두 조회(내것만 체크하면 되니깐 따로 차단 체크 필요 없음)")
    @GetMapping("/place/list")
    public ResponseEntity<CommonResponse> getPlaceList(@AuthenticationPrincipal User user, @RequestParam(value = "groupType") GroupType groupType)
    {
        List<MarkedPlaceReponseDto> placeReponseDtos = placeService.displayMarkedPlace(Long.parseLong(user.getUsername()), groupType);
        return ResponseEntity.ok(new CommonResponse<>(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS,new CommonDetailResponse(PlaceAssembler.markedPlaceReponse(placeReponseDtos))));
    }


    @Operation(summary = "검색창에서 메모리 제목 검색 후, 메모리가 포함된 장소로 이동")
    @GetMapping("/place/{placeId}/search")
    public ResponseEntity<CommonResponse> getPlaceSearchByMemory(@AuthenticationPrincipal User user, @PathVariable Long placeId) {

        PlaceResponseDto placeResponseDto = placeService.findPlaceByPlaceId(Long.parseLong(user.getUsername()), placeId);
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS, new CommonDetailResponse<>(PlaceAssembler.placeResponse(placeResponseDto))));
    }


    @Operation(summary = "특정 장소 조회", description = "특정 장소를 클릭하면 스크랩 여부, 내 메모리 개수, 타인이 전체 공개한 메모리 개수가 보입니다.")
    @GetMapping("/place")
    public ResponseEntity<CommonResponse> getDetailPlace(@AuthenticationPrincipal User user, PlaceSimpleRequest placeSimpleRequest) {

        PlaceResponseDto placeByPosition = placeService.findPlaceByPosition(Long.parseLong(user.getUsername()), placeSimpleRequest.getLat(), placeSimpleRequest.getLng());
        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS, new CommonResponse<>(HttpStatus.OK.value(), MessageConstant.MESSAGE_SUCCESS, new CommonDetailResponse<>(PlaceAssembler.placeResponse(placeByPosition)))));
    }
}
