package cmc.mellyserver.domain.scrap.query;

import static cmc.mellyserver.dbcore.place.QPlace.*;
import static cmc.mellyserver.dbcore.scrap.QPlaceScrap.*;
import static cmc.mellyserver.dbcore.user.QUser.*;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import cmc.mellyserver.dbcore.place.Position;
import cmc.mellyserver.dbcore.scrap.ScrapType;
import cmc.mellyserver.domain.scrap.query.dto.PlaceScrapCountResponseDto;
import cmc.mellyserver.domain.scrap.query.dto.ScrapedPlaceResponseDto;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PlaceScrapQueryRepository {

  private final JPAQueryFactory query;

  public Boolean checkCurrentLoginUserScrapedPlaceByPosition(Long id, Position position) {

	Integer result = query.selectOne()
		.from(placeScrap)
		.innerJoin(place)
		.on(place.id.eq(placeScrap.place.id))
		.fetchJoin()
		.innerJoin(user)
		.on(placeScrap.user.id.eq(user.id))
		.fetchJoin()
		.where(place.position.eq(position), placeScrap.user.id.eq(id))
		.fetchOne();

	return result != null;
  }

  public Boolean checkCurrentLoginUserScrapedPlaceByPlaceId(Long id, Long placeId) {

	Integer result = query.selectOne()
		.from(placeScrap)
		.innerJoin(place)
		.on(place.id.eq(placeScrap.place.id))
		.fetchJoin()
		.innerJoin(user)
		.on(placeScrap.user.id.eq(user.id))
		.fetchJoin()
		.where(place.id.eq(placeId), placeScrap.user.id.eq(id))
		.fetchOne();

	return result != null;
  }

  public List<PlaceScrapCountResponseDto> getScrapedPlaceGrouping(Long id) {

	return query
		.select(Projections.fields(PlaceScrapCountResponseDto.class, placeScrap.scrapType,
			placeScrap.count().as("scrapCount")))
		.from(placeScrap)
		.innerJoin(user)
		.on(placeScrap.user.id.eq(user.id))
		.fetchJoin()
		.where(placeScrap.user.id.eq(id))
		.groupBy(placeScrap.scrapType)
		.fetch();
  }

  public Slice<ScrapedPlaceResponseDto> getUserScrapedPlace(Long lastId, Pageable pageable, Long userId,
	  ScrapType scrapType) {

	List<ScrapedPlaceResponseDto> results = query
		.select(Projections.fields(ScrapedPlaceResponseDto.class, place.id, place.position, place.category,
			place.name, place.imageUrl))
		.from(placeScrap)
		.innerJoin(placeScrap.place, place)
		.innerJoin(placeScrap.user, user)
		.where(ltPlaceId(lastId), placeScrap.user.id.eq(userId), placeScrap.scrapType.eq(scrapType))
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

	if (placeId == -1L) {
	  return null;
	}

	return place.id.lt(placeId);
  }

}
