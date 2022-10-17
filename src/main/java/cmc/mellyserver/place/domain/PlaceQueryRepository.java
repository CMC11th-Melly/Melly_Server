package cmc.mellyserver.place.domain;

import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.domain.QMemory;
import cmc.mellyserver.user.domain.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.util.List;

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


    // 그 유저의 메모리가 하나라도 있는 장소
    public List<Place> getPlaceUserMemoryExist(User user)
    {
        // 1. 메모리를 저장할때 마다 유저가 방문한 장소를 set으로 id값만 저장한다.
        // 2. 같은 장소에 메모리를 써도 만약 장소 id가 같다면 중복으로 기록 안됨
        return query.select(place)
                .from(place)
                .where(place.id.in(user.getVisitedPlace()))
                .fetch();

    }





}
