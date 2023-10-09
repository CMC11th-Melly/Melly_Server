package cmc.mellyserver.domain.scrap;


import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.dbcore.place.Position;
import cmc.mellyserver.dbcore.scrap.PlaceScrap;
import cmc.mellyserver.dbcore.scrap.enums.ScrapType;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.domain.place.PlaceReader;
import cmc.mellyserver.domain.scrap.dto.request.CreatePlaceScrapRequestDto;
import cmc.mellyserver.domain.scrap.dto.response.ScrapedPlaceListResponse;
import cmc.mellyserver.domain.scrap.query.dto.PlaceScrapCountResponseDto;
import cmc.mellyserver.domain.scrap.query.dto.ScrapedPlaceResponseDto;
import cmc.mellyserver.domain.user.UserReader;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceScrapService {

    private final UserReader userReader;

    private final PlaceReader placeReader;

    private final PlaceScrapReader placeScrapReader;

    private final PlaceScrapWriter placeScrapWriter;


    public ScrapedPlaceListResponse findScrapedPlace(Long lastId, Pageable pageable, Long userId, ScrapType scrapType) {

        Slice<ScrapedPlaceResponseDto> userScrapedPlace = placeScrapReader.getUserScrapedPlace(lastId, pageable, userId, scrapType);
        return ScrapedPlaceListResponse.from(userScrapedPlace.getContent(), userScrapedPlace.hasNext());
    }


    @Cacheable(value = "scrap-count:user-id", key = "#userId")
    public List<PlaceScrapCountResponseDto> countByPlaceScrapType(Long userId) {

        return placeScrapReader.getScrapedPlaceGrouping(userId);
    }


    @CachePut(value = "scrap-count:user-id", key = "#createPlaceScrapRequestDto.id")
    @Transactional
    public void createScrap(CreatePlaceScrapRequestDto createPlaceScrapRequestDto) {

        Place place = placeReader.findByPosition(new Position(createPlaceScrapRequestDto.getLat(), createPlaceScrapRequestDto.getLng()));
        User user = userReader.findById(createPlaceScrapRequestDto.getId());

        checkScrapDuplicated(user.getId(), place.getId());

        placeScrapWriter.save(PlaceScrap.createScrap(user, place, createPlaceScrapRequestDto.getScrapType()));
    }


    @CachePut(value = "scrap-count:user-id", key = "#userId")
    @Transactional
    public void removeScrap(Long userId, Position position) {

        Place place = placeReader.findByPosition(position);
        checkScrapExisted(userId, place.getId());

        placeScrapWriter.deleteByUserIdAndPlacePosition(userId, position);
    }


    private void checkScrapDuplicated(Long userId, Long placeId) {

        placeScrapReader.findByUserIdAndPlaceId(userId, placeId).ifPresent(x -> {
            throw new BusinessException(ErrorCode.DUPLICATE_SCRAP);
        });
    }


    private void checkScrapExisted(Long userId, Long placeId) {

        placeScrapReader.findByUserIdAndPlaceId(userId, placeId).orElseThrow(() -> {
            throw new BusinessException(ErrorCode.NOT_EXIST_SCRAP);
        });
    }
}
