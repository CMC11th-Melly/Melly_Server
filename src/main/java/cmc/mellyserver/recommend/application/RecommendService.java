package cmc.mellyserver.recommend.application;

import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.group.domain.group.GroupRepository;
import cmc.mellyserver.common.enums.GroupType;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.repository.PlaceQueryRepository;
import cmc.mellyserver.recommend.application.dto.RecommendResponseDto;
import cmc.mellyserver.user.domain.User;
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
    private final AuthenticatedUserChecker authenticatedUserChecker;
    public List<RecommendResponseDto> getRecommend(Long userSeq)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(userSeq);
        List<Place> recommendPlace = placeQueryRepository.getRecommendPlace(List.of(1L, 2L, 3L));

        return recommendPlace.stream().map
                (t -> new RecommendResponseDto(t.getId(),t.getPlaceImage(),
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
