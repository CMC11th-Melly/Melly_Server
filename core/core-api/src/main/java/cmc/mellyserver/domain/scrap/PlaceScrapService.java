package cmc.mellyserver.domain.scrap;


import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.dbcore.place.PlaceRepository;
import cmc.mellyserver.dbcore.place.Position;
import cmc.mellyserver.dbcore.scrap.PlaceScrap;
import cmc.mellyserver.dbcore.scrap.PlaceScrapRepository;
import cmc.mellyserver.dbcore.scrap.enums.ScrapType;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.UserRepository;
import cmc.mellyserver.domain.scrap.dto.request.CreatePlaceScrapRequestDto;
import cmc.mellyserver.domain.scrap.dto.response.ScrapedPlaceListResponse;
import cmc.mellyserver.domain.scrap.query.PlaceScrapQueryRepository;
import cmc.mellyserver.domain.scrap.query.dto.PlaceScrapCountResponseDto;
import cmc.mellyserver.domain.scrap.query.dto.ScrapedPlaceResponseDto;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PlaceScrapService {

    private final PlaceRepository placeRepository;

    private final UserRepository userRepository;

    private final PlaceScrapQueryRepository placeScrapQueryRepository;

    private final PlaceScrapRepository placeScrapRepository;


    /*
    캐싱 적용 가능 여부 : 불필요
    no offset 기반으로 충분히 최적화를 했기에 캐시 관리로 인한 비용이 더 크다고 판단됨.
     */
    @Transactional(readOnly = true)
    public ScrapedPlaceListResponse findScrapedPlace(Long lastId, Pageable pageable, Long userId, ScrapType scrapType) {

        Slice<ScrapedPlaceResponseDto> userScrapedPlace = placeScrapQueryRepository.getUserScrapedPlace(lastId, pageable, userId, scrapType);

        List<ScrapedPlaceResponseDto> contents = userScrapedPlace.getContent();
        boolean next = userScrapedPlace.hasNext();
        return ScrapedPlaceListResponse.from(contents, next);
    }

    /*
    캐싱 적용 가능 여부 : 가능
    */
    @Cacheable(value = "scrap-count:user-id", key = "#userId")
    @Transactional(readOnly = true)
    public List<PlaceScrapCountResponseDto> countByPlaceScrapType(Long userId) {

        User user = userRepository.getById(userId);
        return placeScrapQueryRepository.getScrapedPlaceGrouping(user.getId());
    }

    /*
    유저별 스크랩 개수 변경으로 인한 Eviction
     */
    @CacheEvict(value = "scrap-count:user-id", key = "#createPlaceScrapRequestDto.id")
    @Transactional
    public void createScrap(CreatePlaceScrapRequestDto createPlaceScrapRequestDto) {

        Optional<Place> placeOpt = placeRepository.findAllByPosition(new Position(createPlaceScrapRequestDto.getLat(), createPlaceScrapRequestDto.getLng()));
        User user = userRepository.getById(createPlaceScrapRequestDto.getId());
        Place place = checkPlaceExist(createPlaceScrapRequestDto, placeOpt);
        placeScrapRepository.save(PlaceScrap.createScrap(user, place, createPlaceScrapRequestDto.getScrapType()));
    }

    /*
    유저별 스크랩 개수 변경으로 인한 Eviction
    */
    @CacheEvict(value = "scrap-count:user-id", key = "#userId")
    @Transactional
    public void removeScrap(Long userId, Double lat, Double lng) {

        Optional<Place> place = placeRepository.findAllByPosition(new Position(lat, lng));
        checkExistScrap(userId, place.get().getId());
        placeScrapRepository.deleteByUserIdAndPlacePosition(userId, new Position(lat, lng));
    }


    private void checkDuplicatedScrap(Long userId, Long placeId) {

        placeScrapRepository.findByUserIdAndPlaceId(userId, placeId).ifPresent(x -> {
            throw new BusinessException(ErrorCode.DUPLICATE_SCRAP);
        });
    }

    private void checkExistScrap(Long userId, Long placeId) {
        placeScrapRepository.findByUserIdAndPlaceId(userId, placeId).orElseThrow(() -> {
            throw new BusinessException(ErrorCode.NOT_EXIST_SCRAP);
        });
    }

    private Place checkPlaceExist(CreatePlaceScrapRequestDto createPlaceScrapRequestDto, Optional<Place> placeOpt) {

        if (placeOpt.isEmpty()) {
            return placeRepository.save(createPlaceScrapRequestDto.toEntity());
        } else {
            checkDuplicatedScrap(createPlaceScrapRequestDto.getId(), placeOpt.get().getId());
            return placeOpt.get();
        }
    }
}
