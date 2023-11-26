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

    public ScrapedPlaceListResponse findScrapedPlace(final Long lastId, final Pageable pageable, final Long userId,
        final ScrapType scrapType) {

        Slice<ScrapedPlaceResponseDto> scrapedPlaces = placeScrapReader.getUserScrapedPlaces(lastId, pageable, userId,
            scrapType);
        return ScrapedPlaceListResponse.of(scrapedPlaces.getContent(), scrapedPlaces.hasNext());
    }

    @Cacheable(cacheNames = CacheNames.SCRAP, key = "#userId")
    public List<PlaceScrapCountResponseDto> countByPlaceScrapType(final Long userId) {

        return placeScrapReader.getScrapedPlaceGrouping(userId);
    }

    @CacheEvict(cacheNames = CacheNames.SCRAP, key = "#userId")
    @CheckPlaceExist
    @Transactional
    public void createScrap(final Long userId, final CreatePlaceScrapRequestDto createPlaceScrapRequestDto) {

        Place place = placeReader.findByPosition(createPlaceScrapRequestDto.getPosition());
        User user = userReader.findById(userId);
        placeScrapValidator.validateDuplicatedScrap(user.getId(), place.getId());
        placeScrapWriter.save(PlaceScrap.createScrap(user, place, createPlaceScrapRequestDto.getScrapType()));
    }

    @CacheEvict(cacheNames = CacheNames.SCRAP, key = "#userId")
    @Transactional
    public void removeScrap(final Long userId, final Position position) {

        Place place = placeReader.findByPosition(position);
        placeScrapValidator.validateExistedScrap(userId, place.getId());
        placeScrapWriter.deleteByUserIdAndPlacePosition(userId, position);
    }
}
