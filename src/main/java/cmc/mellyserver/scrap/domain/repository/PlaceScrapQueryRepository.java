package cmc.mellyserver.scrap.domain.repository;

import cmc.mellyserver.place.domain.Position;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import static cmc.mellyserver.place.domain.QPlace.place;
import static cmc.mellyserver.scrap.domain.QPlaceScrap.placeScrap;

@Repository
public class PlaceScrapQueryRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    // 인텔리제이 인식 오류, 무시하고 진행하면 된다.
    public PlaceScrapQueryRepository(EntityManager em)
    {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }
    public Boolean checkCurrentLoginUserScrapedPlaceByPosition(Long userSeq, Position position)
    {
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

    public Boolean checkCurrentLoginUserScrapedPlaceByPlaceId(Long userSeq, Long placeId)
    {
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



}
