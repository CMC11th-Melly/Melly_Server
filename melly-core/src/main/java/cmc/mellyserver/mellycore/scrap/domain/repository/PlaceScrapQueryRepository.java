package cmc.mellyserver.mellycore.scrap.domain.repository;

import cmc.mellyserver.mellycore.place.domain.Position;
import cmc.mellyserver.mellycore.scrap.domain.repository.dto.PlaceScrapCountResponseDto;
import cmc.mellyserver.mellycore.scrap.domain.repository.dto.ScrapedPlaceResponseDto;
import cmc.mellyserver.mellycore.scrap.domain.enums.ScrapType;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static cmc.mellyserver.mellycore.place.domain.QPlace.place;
import static cmc.mellyserver.mellycore.scrap.domain.QPlaceScrap.placeScrap;
import static cmc.mellyserver.mellycore.user.domain.QUser.user;

@Repository
public class PlaceScrapQueryRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public PlaceScrapQueryRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    public Boolean checkCurrentLoginUserScrapedPlaceByPosition(Long id, Position position) {

        Integer result = query.selectOne()
                .from(placeScrap)
                .innerJoin(place).on(place.id.eq(placeScrap.place.id)).fetchJoin()
                .innerJoin(user).on(placeScrap.user.id.eq(user.id)).fetchJoin()
                .where(
                        place.position.eq(position),
                        placeScrap.user.id.eq(id)
                )
                .fetchOne();

        return result != null;
    }

    public Boolean checkCurrentLoginUserScrapedPlaceByPlaceId(Long id, Long placeId) {

        Integer result = query.selectOne()
                .from(placeScrap)
                .innerJoin(place).on(place.id.eq(placeScrap.place.id)).fetchJoin()
                .innerJoin(user).on(placeScrap.user.id.eq(user.id)).fetchJoin()
                .where(
                        place.id.eq(placeId),
                        placeScrap.user.id.eq(id)
                )
                .fetchOne();

        return result != null;
    }

    public List<PlaceScrapCountResponseDto> getScrapedPlaceGrouping(Long id) {

        return query.select(Projections.fields(PlaceScrapCountResponseDto.class,
                        placeScrap.scrapType,
                        placeScrap.count().as("scrapCount"))
                )
                .from(placeScrap)
                .innerJoin(user).on(placeScrap.user.id.eq(user.id)).fetchJoin()
                .where(placeScrap.user.id.eq(id))
                .groupBy(placeScrap.scrapType)
                .fetch();
    }

    public Slice<ScrapedPlaceResponseDto> getUserScrapedPlace(Long lastId, Pageable pageable, Long userId, ScrapType scrapType) {

        List<ScrapedPlaceResponseDto> results = query.select(Projections.fields(ScrapedPlaceResponseDto.class,
                        place.id,
                        place.position,
                        place.placeCategory,
                        place.placeName,
                        place.placeImage))
                .from(placeScrap)
                .innerJoin(placeScrap.place, place).fetchJoin()
                .innerJoin(placeScrap.user, user).fetchJoin()
                .where(
                        ltPlaceId(lastId),
                        placeScrap.user.id.eq(userId),
                        placeScrap.scrapType.eq(scrapType)
                )
                .orderBy(place.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;

        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

    private BooleanExpression ltPlaceId(Long placeId) {

        if (placeId == null) {
            return null;
        }

        return place.id.lt(placeId);
    }
}
