package cmc.mellyserver.domain.place.query;

import static cmc.mellyserver.dbcore.memory.QMemory.*;
import static cmc.mellyserver.dbcore.place.QPlace.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import cmc.mellyserver.dbcore.group.GroupType;
import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.domain.memory.query.dto.FindPlaceByMemoryTitleResponseDto;
import lombok.RequiredArgsConstructor;

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

  public List<FindPlaceByMemoryTitleResponseDto> searchPlaceByContainMemoryName(Long userId, String memoryName) {

	return query
		.select(Projections.constructor(FindPlaceByMemoryTitleResponseDto.class, memory.placeId, memory.title))
		.from(memory)
		.where(memory.userId.eq(userId), memory.title.contains(memoryName))
		.distinct()
		.fetch();
  }

}