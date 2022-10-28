package cmc.mellyserver.place.domain;

import cmc.mellyserver.common.util.jpa.QueryDslUtil;
import cmc.mellyserver.memory.domain.QMemory;
import cmc.mellyserver.place.domain.enums.ScrapType;
import cmc.mellyserver.placeScrap.application.dto.PlaceScrapResponseDto;
import cmc.mellyserver.placeScrap.domain.PlaceScrap;
import cmc.mellyserver.placeScrap.domain.QPlaceScrap;
import cmc.mellyserver.user.domain.User;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cmc.mellyserver.memory.domain.QMemory.*;
import static cmc.mellyserver.place.domain.QPlace.*;
import static cmc.mellyserver.placeScrap.domain.QPlaceScrap.*;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;

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


    public Place getPlaceByMemory(Long placeId)
    {
        return  query.select(place)
                .from(memory)
                .innerJoin(memory.place,place)
                .where(place.id.eq(placeId))
                .distinct()
                .fetchOne();
    }


    public List<Place> getPlaceUserMemoryExist(User user)
    {
        return query.select(place)
                .from(place)
                .where(place.id.in(user.getVisitedPlace()))
                .fetch();
    }


    /**
     * 스크랩 타입별 유저가 스크랩한 장소 개수 조회
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
     * 유저가 스크랩한 장소 조회
     * @param scrapType 스크랩 타입별로 필터링
     */
    public Slice<Place> getScrapedPlace(Pageable pageable, User user, ScrapType scrapType)
    {
        List<Place> results = query.select(place)
                .from(placeScrap)
                .join(placeScrap.place,place)
                .where(
                        place.id.in(user.getPlaceScraps().stream().map(s -> s.getPlace().getId()).collect(Collectors.toList())),
                        eqScrapType(scrapType)
                ).orderBy(place.id.desc())
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
