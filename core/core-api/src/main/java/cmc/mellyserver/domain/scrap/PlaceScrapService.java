package cmc.mellyserver.domain.scrap;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cmc.mellyserver.common.aspect.place.CheckPlaceExist;
import cmc.mellyserver.config.cache.CacheNames;
import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.dbcore.place.Position;
import cmc.mellyserver.dbcore.scrap.PlaceScrap;
import cmc.mellyserver.dbcore.scrap.ScrapType;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.domain.place.PlaceReader;
import cmc.mellyserver.domain.scrap.dto.request.CreatePlaceScrapRequestDto;
import cmc.mellyserver.domain.scrap.dto.response.ScrapedPlaceListResponse;
import cmc.mellyserver.domain.scrap.query.dto.PlaceScrapCountResponseDto;
import cmc.mellyserver.domain.scrap.query.dto.ScrapedPlaceResponseDto;
import cmc.mellyserver.domain.user.UserReader;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceScrapService {

    private final UserReader userReader;

    private final PlaceReader placeReader;

    private final PlaceScrapReader placeScrapReader;

    private final PlaceScrapWriter placeScrapWriter;

    private final PlaceScrapValidator placeScrapValidator;

    public ScrapedPlaceListResponse findScrapedPlace(Long lastId, Pageable pageable, Long userId, ScrapType scrapType) {
        Slice<ScrapedPlaceResponseDto> places = placeScrapReader.getUserScrapedPlaces(lastId, pageable, userId,
            scrapType);
        return ScrapedPlaceListResponse.of(places.getContent(), places.hasNext());
    }

    @Cacheable(cacheNames = CacheNames.SCRAP, key = "#userId")
    public List<PlaceScrapCountResponseDto> countByPlaceScrapType(Long userId) {
        return placeScrapReader.getScrapedPlaceGrouping(userId);
    }

    @CheckPlaceExist
    @CacheEvict(cacheNames = CacheNames.SCRAP, key = "#userId")
    @Transactional
    public void createScrap(Long userId, CreatePlaceScrapRequestDto createPlaceScrapRequestDto) {

        Place place = placeReader.read(createPlaceScrapRequestDto.getPosition());
        User user = userReader.findById(userId);
        placeScrapValidator.validateDuplicatedScrap(user.getId(), place.getId());
        placeScrapWriter.save(PlaceScrap.createScrap(user, place, createPlaceScrapRequestDto.getScrapType()));
    }

    @CacheEvict(cacheNames = CacheNames.SCRAP, key = "#userId")
    @Transactional
    public void removeScrap(Long userId, Position position) {

        Place place = placeReader.read(position);
        placeScrapValidator.validateExistedScrap(userId, place.getId());
        placeScrapWriter.deleteByUserIdAndPlacePosition(userId, position);
    }
}
