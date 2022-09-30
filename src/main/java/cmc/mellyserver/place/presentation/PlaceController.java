package cmc.mellyserver.place.presentation;

import cmc.mellyserver.common.response.CommonResponse;
import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.place.application.PlaceService;
import cmc.mellyserver.place.presentation.dto.PlaceDetailResponseWrapper;
import cmc.mellyserver.place.presentation.dto.PlaceListReponseDto;
import cmc.mellyserver.place.presentation.dto.PlaceListResponseWrapper;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "특정 좌표 영역 내의 장소 데이터를 모두 조회")
    @GetMapping("/place/list")
    public ResponseEntity<CommonResponse<PlaceListResponseWrapper>> getPlaceList(@AuthenticationPrincipal User user, @RequestParam(value = "groupType") GroupType groupType)
    {
        List<PlaceListReponseDto> placeList = placeService.getPlaceList(user.getUsername(), groupType);
        return ResponseEntity.ok(new CommonResponse<>(200,"유저가 메모리 작성한 장소 조회",new PlaceListResponseWrapper(placeList)));
    }

    @Operation(summary = "특정 장소 상세 조회")
    @GetMapping("/place/{placeId}")
    public ResponseEntity<CommonResponse<PlaceDetailResponseWrapper>> getDetailPlace( @AuthenticationPrincipal User user, @PathVariable Long placeId)
    {
         return ResponseEntity.ok(new CommonResponse(200,"장소 상세 조회",new PlaceDetailResponseWrapper(placeService.getPlaceInfo(placeId,user.getUsername()))));
    }







}
