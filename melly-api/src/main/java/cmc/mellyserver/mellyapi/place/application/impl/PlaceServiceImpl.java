package cmc.mellyserver.mellyapi.place.application.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cmc.mellyserver.mellyapi.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.mellyapi.common.exception.GlobalBadRequestException;
import cmc.mellyserver.mellyapi.place.application.PlaceService;
import cmc.mellyserver.mellyapi.place.application.dto.MarkedPlaceResponseDto;
import cmc.mellyserver.mellyapi.place.application.dto.PlaceResponseDto;
import cmc.mellyserver.mellycore.common.enums.GroupType;
import cmc.mellyserver.mellycore.memory.domain.repository.MemoryQueryRepository;
import cmc.mellyserver.mellycore.place.domain.Place;
import cmc.mellyserver.mellycore.place.domain.Position;
import cmc.mellyserver.mellycore.place.domain.repository.PlaceQueryRepository;
import cmc.mellyserver.mellycore.place.domain.repository.PlaceRepository;
import cmc.mellyserver.mellycore.scrap.domain.repository.PlaceScrapQueryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceServiceImpl implements PlaceService {

	private final PlaceQueryRepository placeQueryRepository;

	private final PlaceRepository placeRepository;

	private final PlaceScrapQueryRepository placeScrapQueryRepository;

	private final MemoryQueryRepository memoryQueryRepository;

	@Override
	public List<MarkedPlaceResponseDto> displayMarkedPlace(Long userSeq, GroupType groupType) {
		List<Place> placeUserMemoryExist = placeQueryRepository.getPlaceUserMemoryExist(userSeq);
		return placeUserMemoryExist.stream()
			.map(each -> new MarkedPlaceResponseDto(each.getPosition(), null, each.getId(), null))
			.collect(Collectors.toList());
	}

	@Override
	public PlaceResponseDto findPlaceByPlaceId(Long userSeq, Long placeId) {
		Place place = placeRepository.findById(placeId).orElseThrow(() -> {
			throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_PLACE);
		});
		HashMap<String, Long> memoryCounts = memoryQueryRepository.countMemoriesBelongToPlace(userSeq, placeId);
		Boolean checkCurrentLoginUserScraped = placeScrapQueryRepository.checkCurrentLoginUserScrapedPlaceByPlaceId(
			placeId, userSeq);

		return new PlaceResponseDto(place.getId(), place.getPosition(), memoryCounts.get("belongToUSer"),
			memoryCounts.get("NotBelongToUSer"),
			checkCurrentLoginUserScraped, place.getPlaceCategory(), place.getPlaceName(), GroupType.ALL,
			place.getPlaceImage());
	}

	@Override
	public PlaceResponseDto findPlaceByPosition(Long userSeq, Double lat, Double lng) {
		Optional<Place> optPlace = placeRepository.findPlaceByPosition(new Position(lat, lng));

		if (optPlace.isEmpty())
			return PlaceResponseDto.PlaceNotCreated(new Position(lat, lng), 0L, 0L, false);
		Place place = optPlace.get();

		HashMap<String, Long> memoryCounts = memoryQueryRepository.countMemoryOfPlace(place.getId(), userSeq);
		Boolean isCurrentLoginUserScraped = placeScrapQueryRepository.checkCurrentLoginUserScrapedPlaceByPosition(
			userSeq, new Position(lat, lng));

		return new PlaceResponseDto(place.getId(), place.getPosition(), memoryCounts.get("myMemoryCount"),
			memoryCounts.get("otherMemoryCount"),
			isCurrentLoginUserScraped, place.getPlaceCategory(), place.getPlaceName(), GroupType.ALL,
			place.getPlaceImage());
	}
}
