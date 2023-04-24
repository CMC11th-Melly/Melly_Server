package cmc.mellyserver.trend.infrastructure;

import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.enums.GroupType;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.repository.PlaceRepository;
import cmc.mellyserver.trend.application.dto.TrendResponseDto;
import cmc.mellyserver.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TrendAnalyzer {

     private final RedisTemplate<String,String> redisTemplate;
     private final PlaceRepository placeRepository;

     public void addKeywordToRedis(String keyword) {
        try {
            System.out.println("랭킹 집어넣을 예정!");
            String keyForRanking = "ranking";
            redisTemplate.opsForZSet().incrementScore(keyForRanking, keyword,1);

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

//     public List<TrendResponseDto> findKeywordSortedByRank(User user) {
//        String keyForRanking = "ranking";
//        ZSetOperations<String,String> ZSetOperations = redisTemplate.opsForZSet();
//        Set<ZSetOperations.TypedTuple<String>> typedTuples = ZSetOperations.reverseRangeWithScores(keyForRanking, 0, 3);
//        return typedTuples.stream().map(s -> {
//
//            Place place = placeRepository.findPlaceByPlaceName(s.getValue()).orElseThrow(() -> {
//
//                throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_PLACE);
//            });
//            // TODO : TREND 분석기에 필요없는 내용임으로 PLACE 내부로 옮길 수 있는지 체크
//            place.setScraped(checkIsScraped(user,place));
//
//            return new TrendResponseDto(place.getId(),
//                    place.getPlaceImage(),
//                    place.getPlaceCategory(),
//                    GroupType.ALL,place.getIsScraped(),
//                    place.getPlaceName(),
//                    place.getMemories(),
//                    "떡잎마을 방범대",
//                    User.builder().build()
//                    );
//        }).collect(Collectors.toList());
//    }

//    private boolean checkIsScraped(User user, Place place)
//    {
//        return user.getPlaceScraps().stream().anyMatch(s -> s.getPlace().getId().equals(place.getId()));
//    }

}
