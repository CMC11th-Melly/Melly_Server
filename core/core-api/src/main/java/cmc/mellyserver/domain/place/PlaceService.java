package cmc.mellyserver.domain.place;

import cmc.mellyserver.dbcore.group.enums.GroupType;
import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.dbcore.place.Position;
import cmc.mellyserver.domain.memory.MemoryReader;
import cmc.mellyserver.domain.memory.query.dto.FindPlaceInfoByMemoryNameResponseDto;
import cmc.mellyserver.domain.scrap.PlaceScrapReader;
import cmc.mellyserver.domain.scrap.dto.MarkedPlaceResponseDto;
import cmc.mellyserver.domain.scrap.dto.PlaceResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceService {

	private final PlaceReader placeReader;

	private final PlaceScrapReader placeScrapReader;

	private final MemoryReader memoryReader;

	public List<MarkedPlaceResponseDto> displayMarkedPlace(Long id, GroupType groupType) {

		List<Place> placeUserMemoryExist = placeReader.placeUserMemoryExist(id, groupType);
		return placeUserMemoryExist.stream()
			.map(each -> new MarkedPlaceResponseDto(each.getPosition(), null, each.getId(), 0L))
			.collect(Collectors.toList());
	}

	public PlaceResponseDto findByPlaceId(Long userId, Long placeId) {

		Place place = placeReader.findById(placeId);
		HashMap<String, Long> memoryCounts = memoryReader.countMemoryInPlace(userId, placeId);
		boolean isUserScraped = placeScrapReader.checkUserScraped(placeId, userId);

		return PlaceResponseDto.of(place, isUserScraped, memoryCounts);
	}

	public PlaceResponseDto findByPosition(Long userId, Position position) {

		Place place = placeReader.findByPosition(position);
		HashMap<String, Long> memoryCounts = memoryReader.countMemoryInPlace(userId, place.getId());
		boolean isUserScraped = placeScrapReader.checkUserScraped(userId, position);

		return PlaceResponseDto.of(place, isUserScraped, memoryCounts);
	}

	public List<FindPlaceInfoByMemoryNameResponseDto> findByMemoryTitle(Long id, String memoryTitle) {

		return placeReader.findByMemoryTitle(id, memoryTitle);
	}

}
