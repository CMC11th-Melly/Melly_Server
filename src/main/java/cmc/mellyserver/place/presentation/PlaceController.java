package cmc.mellyserver.place.presentation;

import cmc.mellyserver.place.application.PlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public void getPlaceList()
    {

    }

    // TODO : 처음에 전체 데이터를 땡겨오는게 좋은 지 아니면 매번 가져오는게 좋은지 판단 필요
    @GetMapping("/place/{placeId}")
    public void getDetailPlace(@PathVariable Long placeId, @AuthenticationPrincipal User user)
    {

    }

    /*
    넓은 범위 내의 장소 위도 경도 값과 ID 값을 불러옴
     */
    @GetMapping("/place")
    public void getPlaceInBoundary()
    {

    }







}
