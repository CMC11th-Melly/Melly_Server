package cmc.mellyserver.place.domain;


import cmc.mellyserver.common.enums.ScrapType;
import cmc.mellyserver.place.placeScrap.application.dto.PlaceScrapResponseDto;

import cmc.mellyserver.user.domain.User;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.stream.Collectors;

import static cmc.mellyserver.memory.domain.QMemory.*;
import static cmc.mellyserver.place.domain.QPlace.*;

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

    // 조건 1 :
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
        return query.select(place)
                .from(place)
                .leftJoin(memory).on(memory.placeId.eq(place.id))
                .where(
                        memory.userId.eq(userSeq)
                )
                .distinct()
                .fetch();
    }



    /**
     * 스크랩 타입별 유저가 스크랩한 장소 개수 조회(최적화 완료, projection 사용으로 fetch join 불가)
     */
    public List<PlaceScrapResponseDto> getScrapedPlaceGrouping(User user)
    {
        return query.select(Projections.fields(PlaceScrapResponseDto.class,placeScrap.scrapType, place.count().as("scrapCount")))
                .from(placeScrap)
                .join(placeScrap.place, place)
                .groupBy(placeScrap.scrapType)
                .where(placeScrap.user.userSeq.eq(user.getUserSeq()))
                .fetch();
    }



    /**
     * 유저가 스크랩한 장소 조회(최적화 완료, 인덱스 설정 필요)
     * fetch join과 paging 동시 사용 불가! batch in 쿼리로 해결해야 함
     */
    public Slice<Place> getScrapedPlace(Pageable pageable, User user, ScrapType scrapType)
    {
        List<Place> results = query.select(place)
                .from(placeScrap)
                .join(placeScrap.place,place)
                .where(
                        place.id.in(user.getPlaceScraps().stream().map(s -> s.getPlace().getId()).collect(Collectors.toList())),
                        eqScrapType(scrapType)
                ).orderBy(place.id.desc()) // 따로 정렬 기준 필요 없으므로 placeId 값으로 정렬함
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, results);
    }



    private BooleanExpression eqScrapType(ScrapType scrapType)
    {
        if(scrapType == null || scrapType.equals(ScrapType.ALL))
        {
            return null;
        }

        return placeScrap.scrapType.eq(scrapType);
    }



    /**
     * 무한 스크롤 페이징 로직
     */
    private Slice<Place> checkLastPage(Pageable pageable, List<Place> results)
    {
        boolean hasNext = false;

        if (results.size() > pageable.getPageSize())
        {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }


}
