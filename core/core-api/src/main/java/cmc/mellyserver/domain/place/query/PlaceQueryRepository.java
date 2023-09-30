package cmc.mellyserver.domain.place.query;


import cmc.mellyserver.dbcore.group.enums.GroupType;
import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.domain.memory.query.dto.FindPlaceInfoByMemoryNameResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static cmc.mellyserver.dbcore.memory.QMemory.memory;
import static cmc.mellyserver.dbcore.place.QPlace.place;


@Repository
public class PlaceQueryRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public PlaceQueryRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }


    public List<Place> getPlaceUserMemoryExist(Long id, GroupType groupType) {

        return query.select(place)
                .from(place)
                .where(
                        JPAExpressions.selectFrom(memory)
                                .where(memory.placeId.eq(place.id), memory.userId.eq(id))
                                .exists()
                )
                .fetch();

    }

    public List<FindPlaceInfoByMemoryNameResponseDto> searchPlaceByContainMemoryName(Long id, String memoryName) {

        return query.select(
                        Projections.constructor(FindPlaceInfoByMemoryNameResponseDto.class, memory.placeId, memory.title))
                .from(memory)
                .where(
                        memory.userId.eq(id),
                        memory.title.contains(memoryName)
                )
                .distinct().fetch();
    }

}