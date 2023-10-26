package cmc.mellyserver.domain.place.query;

import cmc.mellyserver.dbcore.group.enums.GroupType;
import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.domain.memory.query.dto.FindPlaceInfoByMemoryNameResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static cmc.mellyserver.dbcore.memory.QMemory.memory;
import static cmc.mellyserver.dbcore.place.QPlace.place;

@Repository
@RequiredArgsConstructor
public class PlaceQueryRepository {

	private final JPAQueryFactory query;

	public List<Place> getPlaceUserMemoryExist(Long id, GroupType groupType) {

		return query.select(place)
			.from(place)
			.where(JPAExpressions.selectFrom(memory).where(memory.placeId.eq(place.id), memory.userId.eq(id)).exists())
			.fetch();

	}

	public List<FindPlaceInfoByMemoryNameResponseDto> searchPlaceByContainMemoryName(Long id, String memoryName) {

		return query
			.select(Projections.constructor(FindPlaceInfoByMemoryNameResponseDto.class, memory.placeId, memory.title))
			.from(memory)
			.where(memory.userId.eq(id), memory.title.contains(memoryName))
			.distinct()
			.fetch();
	}

}