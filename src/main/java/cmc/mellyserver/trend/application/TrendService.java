package cmc.mellyserver.trend.application;

import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.group.domain.GroupRepository;
import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.PlaceQueryRepository;
import cmc.mellyserver.trend.application.dto.TrendResponseDto;
import cmc.mellyserver.trend.infrastructure.TrendAnalyzer;
import cmc.mellyserver.user.domain.User;
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
           User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
           List<Place> trendingPlace = placeQueryRepository.getTrendingPlace(List.of(1L, 2L, 3L));

//        return trendAnalyzer.findKeywordSortedByRank(user);
        return trendingPlace.stream().map
                (t -> new TrendResponseDto(t.getId(),t.getPlaceImage(),
                        t.getPlaceCategory(),
                        GroupType.FRIEND,
                        checkIsScraped(user,t),
                        t.getPlaceName(),t.getMemories(),
                        groupRepository.findById(t.getMemories().get(0).getGroupInfo().getGroupId()).get().getGroupName(),
                        user
                ))
                .collect(Collectors.toList());
    }

    private boolean checkIsScraped(User user, Place place)
    {
        return user.getPlaceScraps().stream().anyMatch(s -> s.getPlace().getId().equals(place.getId()));
    }

}
