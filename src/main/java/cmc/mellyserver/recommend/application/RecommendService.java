package cmc.mellyserver.recommend.application;

import cmc.mellyserver.group.domain.GroupRepository;
import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.PlaceQueryRepository;
import cmc.mellyserver.place.domain.PlaceRepository;
import cmc.mellyserver.recommend.application.dto.RecommendResponseDto;
import cmc.mellyserver.trend.application.dto.TrendResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecommendService {


    private final GroupRepository groupRepository;
    private final PlaceQueryRepository placeQueryRepository;
    public List<RecommendResponseDto> getRecommend()
    {

        List<Place> recommendPlace = placeQueryRepository.getRecommendPlace(List.of(1L, 2L, 3L));
        return recommendPlace.stream().map
                (t -> new RecommendResponseDto(t.getId(),t.getPlaceImage(),
                        "카페, 디저트",
                        GroupType.FRIEND,
                        false,
                        t.getPlaceName(),t.getMemories(),
                        groupRepository.findById(t.getMemories().get(0).getGroupInfo().getGroupId()).get().getGroupName()
                        ))
                .collect(Collectors.toList());
    }
}
