package cmc.mellyserver.place.application.impl;


import cmc.mellyserver.common.enums.GroupType;
import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.memory.domain.repository.MemoryQueryRepository;
import cmc.mellyserver.place.application.PlaceService;
import cmc.mellyserver.place.application.dto.PlaceResponseDto;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.Position;
import cmc.mellyserver.place.domain.repository.PlaceQueryRepository;
import cmc.mellyserver.place.domain.repository.PlaceRepository;
import cmc.mellyserver.place.application.dto.MarkedPlaceResponseDto;
import cmc.mellyserver.scrap.domain.repository.PlaceScrapQueryRepository;
import cmc.mellyserver.place.presentation.dto.PlaceAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceServiceImpl implements PlaceService {

    private final PlaceQueryRepository placeQueryRepository;

    private final PlaceRepository placeRepository;

    private final PlaceScrapQueryRepository placeScrapQueryRepository;

    private final MemoryQueryRepository memoryQueryRepository;


    @Override
    public List<MarkedPlaceResponseDto> displayMarkedPlace(Long userSeq, GroupType groupType)
    {
        List<Place> placeUserMemoryExist = placeQueryRepository.getPlaceUserMemoryExist(userSeq);
        return PlaceAssembler.markedPlaceResponseDto(placeUserMemoryExist);
    }


    @Override
    public PlaceResponseDto findPlaceByPlaceId(Long userSeq,Long placeId)
    {
        Place place = placeRepository.findById(placeId).orElseThrow(() -> {throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_PLACE);});
        HashMap<String, Long> memoryCounts = memoryQueryRepository.countMemoriesBelongToPlace(userSeq, placeId);
        Boolean checkCurrentLoginUserScraped = placeScrapQueryRepository.checkCurrentLoginUserScrapedPlaceByPlaceId(placeId, userSeq);
        return PlaceAssembler.placeResponseDto(place,checkCurrentLoginUserScraped,memoryCounts);
    }


    @Override
    public PlaceResponseDto findPlaceByPosition(Long userSeq, Double lat, Double lng)
    {
        Optional<Place> optPlace = placeRepository.findPlaceByPosition(new Position(lat, lng));

        if(optPlace.isEmpty()) return PlaceResponseDto.PlaceNotCreated(new Position(lat, lng), 0L, 0L, false);
        Place place = optPlace.get();

        HashMap<String, Long> memoryCounts = memoryQueryRepository.countMemoryOfPlace(place.getId(), userSeq);
        Boolean isCurrentLoginUserScraped = placeScrapQueryRepository.checkCurrentLoginUserScrapedPlaceByPosition(userSeq, new Position(lat, lng));

        return new PlaceResponseDto(place.getId(), place.getPosition(), memoryCounts.get("myMemoryCount"), memoryCounts.get("otherMemoryCount"),
                isCurrentLoginUserScraped, place.getPlaceCategory(), place.getPlaceName(), GroupType.ALL, place.getPlaceImage());
    }
}
