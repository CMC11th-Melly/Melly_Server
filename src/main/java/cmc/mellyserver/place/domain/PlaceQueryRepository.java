package cmc.mellyserver.place.domain;

import cmc.mellyserver.common.util.jpa.QueryDslUtil;
import cmc.mellyserver.memory.domain.QMemory;
import cmc.mellyserver.placeScrap.domain.PlaceScrap;
import cmc.mellyserver.placeScrap.domain.QPlaceScrap;
import cmc.mellyserver.user.domain.User;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
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

    // 인텔리제이 인식 오류, 무시하고 진행하면 된다.
    public PlaceQueryRepository(EntityManager em)
    {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }


    public List<Place> getTrendingPlace(List<Long> placeIds)
    {
        return query.select(place)
                .from(place)
                .where(place.id.in(placeIds))
                .fetch();
    }


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


    public Slice<Place> getScrapedPlace(Pageable pageable, User user)
    {
        List<Place> results = query.select(place)
                .from(place)
                .where(

                        place.id.in(user.getPlaceScraps().stream().map(s -> s.getPlace().getId()).collect(Collectors.toList()))
                ).orderBy(place.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();
        return checkLastPage(pageable, results);
    }

    public List<Place> getScrapedPlaceGrouping(User user)
    {
        List<Tuple> fetch = query.select(placeScrap.scrapType, place.count())
                .from(placeScrap)
                .join(placeScrap.place, place)
                .fetch();
        System.out.println(fetch);
        return null;


    }


    private BooleanExpression ltPlaceId(Long placeId) {
        if (placeId == null) {
            return null;
        }

        return place.id.lt(placeId);
    }

    private Slice<Place> checkLastPage(Pageable pageable, List<Place> results) {

        boolean hasNext = false;

        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }


//    private List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {
//        List<OrderSpecifier> ORDERS = new ArrayList<>();
//
//        if (!isEmpty(pageable.getSort())) {
//            for (Sort.Order order : pageable.getSort()) {
//                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
//
//                switch (order.getProperty()) {
//                    case "createdDate":
//                        OrderSpecifier<?> visitedDate = QueryDslUtil
//                                .getSortedColumn(direction, memory, "visitedDate");
//                        ORDERS.add(visitedDate);
//                        break;
//                    default:
//                        break;
//                }
//            }
//        }
//
//        return ORDERS;
//    }

}
