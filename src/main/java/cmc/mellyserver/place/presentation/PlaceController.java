package cmc.mellyserver.place.presentation;

import cmc.mellyserver.group.domain.GroupType;
import cmc.mellyserver.place.application.PlaceService;
import cmc.mellyserver.place.domain.service.GetPlaceInfoDto;
import cmc.mellyserver.place.presentation.dto.PlaceGroupCond;
import cmc.mellyserver.place.presentation.dto.PlaceListReponseDto;
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

    /*
    현재 지도 범위 내의 장소 데이터를 모두 불러옴
     */
    @GetMapping("/place/list")
    public ResponseEntity<List<PlaceListReponseDto>> getPlaceList(@AuthenticationPrincipal User user, @RequestParam(value = "groupType") GroupType groupType)
    {
        log.info("인증 유저 id = {}",user.getUsername());
        List<PlaceListReponseDto> placeList = placeService.getPlaceList(user.getUsername(), groupType);
        return ResponseEntity.ok(placeList);
    }

    // TODO : 처음에 전체 데이터를 땡겨오는게 좋은 지 아니면 매번 가져오는게 좋은지 판단 필요
    @GetMapping("/place/{placeId}")
    public ResponseEntity<GetPlaceInfoDto> getDetailPlace(@PathVariable Long placeId, @AuthenticationPrincipal User user)
    {
         return ResponseEntity.ok(placeService.getPlaceInfo(placeId,user));
    }

    /*
    넓은 범위 내의 장소 위도 경도 값과 ID 값을 불러옴
     */
    @GetMapping("/place")
    public void getPlaceInBoundary()
    {

    }







}
