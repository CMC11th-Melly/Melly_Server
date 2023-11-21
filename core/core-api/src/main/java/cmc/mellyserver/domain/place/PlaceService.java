package cmc.mellyserver.domain.place;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cmc.mellyserver.dbcore.group.GroupType;
import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.dbcore.place.Position;
import cmc.mellyserver.domain.memory.MemoryReader;
import cmc.mellyserver.domain.memory.query.dto.FindPlaceByMemoryTitleResponseDto;
import cmc.mellyserver.domain.scrap.dto.MarkedPlaceResponseDto;
import cmc.mellyserver.domain.scrap.dto.PlaceResponseDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceService {

    private final PlaceReader placeReader;

    private final MemoryReader memoryReader;

    private final UserScrapChecker userScrapChecker;

    public List<MarkedPlaceResponseDto> displayMarkedPlace(final Long userId, final GroupType groupType) {
        return placeReader.placeUserMemoryExist(userId, groupType);
    }

    public PlaceResponseDto findByPlaceId(final Long userId, final Long placeId) {
        Place place = placeReader.findById(placeId);
        HashMap<String, Long> memoryCounts = memoryReader.countMemoryInPlace(userId, placeId);
        boolean isUserScraped = userScrapChecker.check(userId, placeId);

        return PlaceResponseDto.of(place, isUserScraped, memoryCounts);
    }

    public PlaceResponseDto findByPosition(final Long userId, final Position position) {
        Place place = placeReader.findByPosition(position);
        HashMap<String, Long> memoryCounts = memoryReader.countMemoryInPlace(userId, place.getId());
        boolean isUserScraped = userScrapChecker.check(userId, position);

        return PlaceResponseDto.of(place, isUserScraped, memoryCounts);
    }

    public List<FindPlaceByMemoryTitleResponseDto> findByMemoryTitle(final Long userId, final String memoryTitle) {
        return placeReader.findByMemoryTitle(userId, memoryTitle);
    }

}
