package cmc.mellyserver.place.domain.repository;

import cmc.mellyserver.common.enums.GroupType;
import cmc.mellyserver.common.enums.ScrapType;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.scrap.application.dto.response.PlaceScrapCountResponseDto;
import cmc.mellyserver.scrap.application.dto.response.ScrapedPlaceResponseDto;
import cmc.mellyserver.user.domain.User;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import static cmc.mellyserver.memory.domain.QMemory.*;
import static cmc.mellyserver.place.domain.QPlace.*;
import static cmc.mellyserver.scrap.domain.QPlaceScrap.placeScrap;

@Repository
public class PlaceQueryRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;


    public PlaceQueryRepository(EntityManager em)
    {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }


    /**
     * 추천 장소 조회 -> 차후에 추천 로직으로 변경 예정
     * TODO : 최종 수정
     */
    public List<Place> getTrendingPlace(List<Long> placeIds)
    {
        return query.select(place)
                .from(place)
                .where(place.id.in(placeIds))
                .fetch();
    }



    /**
     * 트렌딩 장소 조회 -> 차후에 레디스 기반 트렌딩 로직으로 변경 예정
     * TODO : 최종 수정
     */
    public List<Place> getRecommendPlace(List<Long> placeIds)
    {
        return query.select(place)
                .from(place)
                .where(place.id.in(placeIds))
                .fetch();
    }



    /**
     * placeId로 장소 조회
     */

    //
    public Place searchPlaceByPlaceId(Long placeId)
    {
        return  query.select(place)
                .from(place)
                .where(place.id.eq(placeId))
                .fetchOne();
    }


    /** TODO : 수정 완료
     * 로그인 유저의 메모리가 존재하는 장소 조회 (최적화 완료), 나의 메모리는 신고 당해도 나한테 보이도록 하기!
     * 차단 당해도 내 메모리는 보여야 하니 해당사항 없음! (차단 필터링 x)
     */
    public List<Place> getPlaceUserMemoryExist(Long userSeq)
    {
        List<Tuple> result = query.select(place.position, place.id)
                .from(place)
                .leftJoin(memory).on(memory.placeId.eq(place.id))
                .where(
                        memory.userId.eq(userSeq)
                )
                .distinct()
                .fetch();
        return null;
    }



    /**
     * 스크랩 타입별 유저가 스크랩한 장소 개수 조회(최적화 완료, projection 사용으로 fetch join 불가)
     * TODO : 수정 완료
     */
    public List<PlaceScrapCountResponseDto> getScrapedPlaceGrouping(User user)
    {
        return query.select(Projections.fields(PlaceScrapCountResponseDto.class, placeScrap.scrapType, place.count().as("scrapCount")))
                .from(placeScrap)
                .join(place).on(placeScrap.place.id.eq(place.id))
                .groupBy(placeScrap.scrapType)
                .where(placeScrap.user.userSeq.eq(user.getUserSeq()))
                .fetch();
    }


    /**
     * 유저가 스크랩한 장소 조회
     */
    public Slice<ScrapedPlaceResponseDto> getScrapedPlace(Pageable pageable, Long userSeq, ScrapType scrapType)
    {
        List<ScrapedPlaceResponseDto> results = query.select(Projections.constructor(ScrapedPlaceResponseDto.class, place.id, place.position, place.placeCategory, place.placeName, place.placeImage))
                .from(place)
                .where(place.id.in(
                                JPAExpressions.select(placeScrap.place.id)
                                        .from(placeScrap)
                                        .where(placeScrap.user.userSeq.eq(userSeq).and(eqScrapType(scrapType)))
                        )
                )
                .orderBy(place.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        List<Long> placeIds = toPlaceIds(results);

        List<Tuple> myMemoryCount = query.select(memory.placeId, memory.count())
                .from(memory)
                .where(
                        memory.placeId.in(placeIds),
                        memory.userId.eq(userSeq)
                )
                .groupBy(memory.placeId)
                .fetch();

        LinkedHashMap<Long, Long> myMemoryCountMap = new LinkedHashMap<>();

        for (Tuple myMemoryTuple : myMemoryCount) {
            Long placeId = myMemoryTuple.get(0, Long.class);
            myMemoryCountMap.put(placeId, myMemoryTuple.get(1,Long.class));
        }

        List<Tuple> otherMemoryCount = query.select(memory.placeId, memory.count())
                .from(memory)
                .where(
                        memory.placeId.in(placeIds),
                        memory.userId.ne(userSeq)
                )
                .groupBy(memory.placeId)
                .fetch();

        LinkedHashMap<Long, Long> otherMemoryCountMap = new LinkedHashMap<>();

        for (Tuple otherMemoryTuple : otherMemoryCount) {
            Long placeId = otherMemoryTuple.get(0, Long.class);
            otherMemoryCountMap.put(placeId, otherMemoryTuple.get(1,Long.class));
        }

        results.stream().forEach(r -> {
            r.setIsScraped(true);
            r.setMyMemoryCount(myMemoryCountMap.get(r.getPlaceId()));
            r.setOtherMemoryCount(otherMemoryCountMap.get(r.getPlaceId()));
            r.setRecommendType(GroupType.ALL);
        });


        boolean hasNext = false;

        if (results.size() > pageable.getPageSize())
        {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }


    private List<Long> toPlaceIds(List<ScrapedPlaceResponseDto> result) {
        return result.stream()
                .map(o -> o.getPlaceId())
                .collect(Collectors.toList());
    }

    private BooleanExpression eqScrapType(ScrapType scrapType)
    {
        if(scrapType == null || scrapType.equals(ScrapType.ALL))
        {
            return null;
        }

        return placeScrap.scrapType.eq(scrapType);
    }

}