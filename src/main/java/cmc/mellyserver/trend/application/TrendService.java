package cmc.mellyserver.trend.application;

import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.group.domain.GroupRepository;
import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.PlaceQueryRepository;
import cmc.mellyserver.trend.application.dto.TrendResponseDto;
import cmc.mellyserver.trend.infrastructure.TrendAnalyzer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TrendService {

    private final GroupRepository groupRepository;
    private final PlaceQueryRepository placeQueryRepository;
    private final TrendAnalyzer trendAnalyzer;
    private final AuthenticatedUserChecker authenticatedUserChecker;

    public List<TrendResponseDto> getTrend(String uid) {
           List<Place> trendingPlace = placeQueryRepository.getTrendingPlace(List.of(1L, 2L, 3L));
//        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
//        return trendAnalyzer.findKeywordSortedByRank(user);
        return trendingPlace.stream().map
                (t -> new TrendResponseDto(t.getId(),t.getPlaceImage(),
                        "카페, 디저트",
                        GroupType.FRIEND,
                        false,
                        t.getPlaceName(),t.getMemories(),
                        groupRepository.findById(t.getMemories().get(0).getGroupInfo().getGroupId()).get().getGroupName()
                ))
                .collect(Collectors.toList());
    }

}
