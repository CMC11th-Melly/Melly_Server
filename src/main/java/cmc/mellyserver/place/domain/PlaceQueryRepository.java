package cmc.mellyserver.place.domain;

import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.domain.QMemory;
import cmc.mellyserver.scrap.domain.QScrap;
import cmc.mellyserver.user.domain.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.security.core.parameters.P;
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


    public Slice<Place> getScrapedPlace(Long lastId, Pageable pageable, User user)
    {
        List<Place> results = query.select(place)
                .from(place)
                .where(
                        ltPlaceId(lastId),
                        place.id.in(user.getScraps().stream().map(s -> s.getPlace().getId()).collect(Collectors.toList()))
                ).orderBy(place.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();
        return checkLastPage(pageable, results);
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

}
