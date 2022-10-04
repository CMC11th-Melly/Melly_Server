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
                        GroupType.FRIEND,
                        false,
                        t.getName(),
                        t.getMemories().get(0).getId(),
                        t.getMemories().get(0).getMemoryImages().stream().map(tm -> tm.getImagePath()).collect(Collectors.toList()),
                        t.getMemories().get(0).getTitle(),
                        t.getMemories().get(0).getContent(),
                        groupRepository.findById(t.getMemories().get(0).getGroupInfo().getGroupId()).get().getGroupName(),
                        t.getMemories().get(0).getStars()))
                .collect(Collectors.toList());
    }
}
