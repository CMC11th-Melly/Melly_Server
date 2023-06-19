package cmc.mellyserver.mellydomain.place.domain.repository;

import static cmc.mellyserver.mellydomain.memory.domain.QMemory.memory;
import static cmc.mellyserver.mellydomain.place.domain.QPlace.place;
import static cmc.mellyserver.mellydomain.scrap.domain.QPlaceScrap.placeScrap;

import cmc.mellyserver.mellydomain.common.enums.GroupType;
import cmc.mellyserver.mellydomain.common.enums.ScrapType;
import cmc.mellyserver.mellydomain.place.domain.Place;
import cmc.mellyserver.mellydomain.scrap.domain.repository.dto.PlaceScrapCountResponseDto;
import cmc.mellyserver.mellydomain.scrap.domain.repository.dto.ScrapedPlaceResponseDto;
import cmc.mellyserver.mellydomain.user.domain.User;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
public class PlaceQueryRepository {

	private final EntityManager em;
	private final JPAQueryFactory query;

	public PlaceQueryRepository(EntityManager em) {
		this.em = em;
		this.query = new JPAQueryFactory(em);
	}


	public List<Place> getPlaceUserMemoryExist(Long userSeq) {

		return query.select(place)
				.from(place)
				.where(
						JPAExpressions.selectFrom(memory)
								.where(memory.placeId.eq(place.id), memory.userId.eq(userSeq))
								.exists()
				)
				.fetch();

	}

	public List<PlaceScrapCountResponseDto> getScrapedPlaceGrouping(User user) {
		return query.select(
						Projections.fields(PlaceScrapCountResponseDto.class, placeScrap.scrapType,
								place.count().as("scrapCount")))
				.from(placeScrap)
				.join(place).on(placeScrap.place.id.eq(place.id))
				.groupBy(placeScrap.scrapType)
				.where(placeScrap.user.userSeq.eq(user.getUserSeq()))
				.fetch();
	}

	public Slice<ScrapedPlaceResponseDto> getScrapedPlace(Pageable pageable, Long userSeq,
			ScrapType scrapType) {
		List<ScrapedPlaceResponseDto> results = query.select(
						Projections.constructor(ScrapedPlaceResponseDto.class, place.id, place.position,
								place.placeCategory,
								place.placeName, place.placeImage))
				.from(place)
				.where(place.id.in(
								JPAExpressions.select(placeScrap.place.id)
										.from(placeScrap)
										.where(placeScrap.user.userSeq.eq(userSeq)
												.and(eqScrapType(scrapType)))
						)
				)
				.orderBy(place.id.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize() + 1)
				.fetch();

		List<Long> placeIds = toPlaceIds(results);

		List<Tuple> myMemoryCount = query.select(memory.placeId, memory.count())
				.from(memory)
				.where(
						memory.placeId.in(placeIds),
						memory.userId.eq(userSeq)
				)
				.groupBy(memory.placeId)
				.fetch();

		LinkedHashMap<Long, Long> myMemoryCountMap = new LinkedHashMap<>();

		for (Tuple myMemoryTuple : myMemoryCount) {
			Long placeId = myMemoryTuple.get(0, Long.class);
			myMemoryCountMap.put(placeId, myMemoryTuple.get(1, Long.class));
		}

		List<Tuple> otherMemoryCount = query.select(memory.placeId, memory.count())
				.from(memory)
				.where(
						memory.placeId.in(placeIds),
						memory.userId.ne(userSeq)
				)
				.groupBy(memory.placeId)
				.fetch();

		LinkedHashMap<Long, Long> otherMemoryCountMap = new LinkedHashMap<>();

		for (Tuple otherMemoryTuple : otherMemoryCount) {
			Long placeId = otherMemoryTuple.get(0, Long.class);
			otherMemoryCountMap.put(placeId, otherMemoryTuple.get(1, Long.class));
		}

		results.stream().forEach(r -> {
			r.setIsScraped(true);
			r.setMyMemoryCount(myMemoryCountMap.get(r.getPlaceId()));
			r.setOtherMemoryCount(otherMemoryCountMap.get(r.getPlaceId()));
			r.setRecommendType(GroupType.ALL);
		});

		boolean hasNext = false;

		if (results.size() > pageable.getPageSize()) {
			hasNext = true;
			results.remove(pageable.getPageSize());
		}

		return new SliceImpl<>(results, pageable, hasNext);
	}

	private List<Long> toPlaceIds(List<ScrapedPlaceResponseDto> result) {
		return result.stream()
				.map(o -> o.getPlaceId())
				.collect(Collectors.toList());
	}

	private BooleanExpression eqScrapType(ScrapType scrapType) {
		if (scrapType == null || scrapType.equals(ScrapType.ALL)) {
			return null;
		}

		return placeScrap.scrapType.eq(scrapType);
	}

}