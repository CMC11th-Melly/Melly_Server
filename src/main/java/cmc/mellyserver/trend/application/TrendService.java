package cmc.mellyserver.trend.application;

import cmc.mellyserver.group.domain.GroupRepository;
import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.PlaceQueryRepository;
import cmc.mellyserver.place.domain.PlaceRepository;
import cmc.mellyserver.trend.application.dto.TrendResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TrendService {

    private final PlaceRepository placeRepository;
    private final GroupRepository groupRepository;
    private final PlaceQueryRepository placeQueryRepository;
    public List<TrendResponseDto> getTrend()
    {
        List<Place> trendingPlace = placeQueryRepository.getTrendingPlace(List.of(1L, 2L, 3L));
        return trendingPlace.stream().map
                (t -> new TrendResponseDto(t.getId(),t.getPlaceImage(),
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
