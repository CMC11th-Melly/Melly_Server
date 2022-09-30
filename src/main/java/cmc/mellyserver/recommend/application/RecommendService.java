package cmc.mellyserver.recommend.application;

import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.PlaceQueryRepository;
import cmc.mellyserver.place.domain.PlaceRepository;
import cmc.mellyserver.recommend.application.dto.RecommendResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecommendService {

    private final PlaceRepository placeRepository;
    private final PlaceQueryRepository placeQueryRepository;
    public List<RecommendResponseDto> getRecommend()
    {
        List<Place> recommendPlace = placeQueryRepository.getRecommendPlace(List.of(1L, 2L, 3L));
        return recommendPlace.stream().map
                (r -> new RecommendResponseDto(r.getId(),r.getPlaceImage(),
                        GroupType.FRIEND,
                        false,
                        r.getName(),
                        r.getMemories().get(0).getId(),
                        r.getMemories().get(0).getMemoryImages().get(0).getImagePath(),
                        r.getMemories().get(0).getTitle(),
                        r.getMemories().get(0).getContent()))
                .collect(Collectors.toList());
    }
}
