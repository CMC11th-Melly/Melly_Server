package cmc.mellyserver.mellycore.scrap.domain.repository;

import cmc.mellyserver.mellycommon.enums.ScrapType;
import cmc.mellyserver.mellycore.place.domain.Position;
import cmc.mellyserver.mellycore.scrap.domain.repository.dto.PlaceScrapCountResponseDto;
import cmc.mellyserver.mellycore.scrap.domain.repository.dto.ScrapedPlaceResponseDto;
import cmc.mellyserver.mellycore.user.domain.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static cmc.mellyserver.mellycore.place.domain.QPlace.place;
import static cmc.mellyserver.mellycore.scrap.domain.QPlaceScrap.placeScrap;

@Repository
public class PlaceScrapQueryRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    // 인텔리제이 인식 오류, 무시하고 진행하면 된다.
    public PlaceScrapQueryRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    public Boolean checkCurrentLoginUserScrapedPlaceByPosition(Long userSeq, Position position) {
        Integer result = query.selectOne()
                .from(placeScrap)
                .leftJoin(place).on(place.id.eq(placeScrap.place.id))
                .where(
                        place.position.eq(position),
                        placeScrap.user.userSeq.eq(userSeq)
                )
                .fetchOne();

        return result != null;
    }

    public Boolean checkCurrentLoginUserScrapedPlaceByPlaceId(Long userSeq, Long placeId) {
        Integer result = query.selectOne()
                .from(placeScrap)
                .leftJoin(place).on(place.id.eq(placeScrap.place.id))
                .where(
                        place.id.eq(placeId),
                        placeScrap.user.userSeq.eq(userSeq)
                )
                .fetchOne();

        return result != null;
    }

    public List<PlaceScrapCountResponseDto> getScrapedPlaceGrouping(Long userSeq) {
        return query.select(
                        Projections.fields(PlaceScrapCountResponseDto.class, placeScrap.scrapType, placeScrap.count().as("scrapCount"))
                )
                .from(placeScrap)
                .groupBy(placeScrap.scrapType)
                .where(placeScrap.user.userSeq.eq(userSeq))
                .fetch();
    }

    public Slice<ScrapedPlaceResponseDto> getUserScrapedPlace(Pageable pageable, Long userSeq, ScrapType scrapType) {

        List<ScrapedPlaceResponseDto> results = query.select(Projections.constructor(ScrapedPlaceResponseDto.class,
                        place.id, place.position,
                        place.placeCategory,
                        place.placeName, place.placeImage))
                .from(placeScrap)
                .leftJoin(placeScrap.place, place)
                .leftJoin(placeScrap.user, QUser.user)
                .where(
                        placeScrap.user.userSeq.eq(userSeq),
                        placeScrap.scrapType.eq(scrapType)
                )
                .orderBy(place.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;

        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }
}
