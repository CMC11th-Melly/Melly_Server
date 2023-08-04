package cmc.mellyserver.mellycore.scrap.application;


import cmc.mellyserver.mellycommon.codes.ErrorCode;
import cmc.mellyserver.mellycommon.enums.ScrapType;
import cmc.mellyserver.mellycore.common.AuthenticatedUserChecker;
import cmc.mellyserver.mellycore.common.exception.BusinessException;
import cmc.mellyserver.mellycore.place.domain.Place;
import cmc.mellyserver.mellycore.place.domain.Position;
import cmc.mellyserver.mellycore.place.domain.repository.PlaceRepository;
import cmc.mellyserver.mellycore.scrap.application.dto.request.CreatePlaceScrapRequestDto;
import cmc.mellyserver.mellycore.scrap.domain.PlaceScrap;
import cmc.mellyserver.mellycore.scrap.domain.repository.PlaceScrapQueryRepository;
import cmc.mellyserver.mellycore.scrap.domain.repository.PlaceScrapRepository;
import cmc.mellyserver.mellycore.scrap.domain.repository.dto.PlaceScrapCountResponseDto;
import cmc.mellyserver.mellycore.scrap.domain.repository.dto.ScrapedPlaceResponseDto;
import cmc.mellyserver.mellycore.user.domain.User;
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

    private final PlaceScrapQueryRepository placeScrapQueryRepository;

    private final PlaceScrapRepository placeScrapRepository;

    private final AuthenticatedUserChecker authenticatedUserChecker;

    /*
    캐싱 적용 가능 여부 : 불필요
    no offset 기반으로 충분히 최적화를 했기에 캐시 관리로 인한 비용이 더 크다고 판단됨.
     */
    @Transactional(readOnly = true)
    public Slice<ScrapedPlaceResponseDto> findScrapedPlace(Long lastId, Pageable pageable, Long userId, ScrapType scrapType) {

        return placeScrapQueryRepository.getUserScrapedPlace(lastId, pageable, userId, scrapType);
    }

    /*
    캐싱 적용 가능 여부 : 가능
    */
    @Cacheable(value = "scrap-count:user-id", key = "#userId")
    @Transactional(readOnly = true)
    public List<PlaceScrapCountResponseDto> countByPlaceScrapType(Long userId) {

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(userId);
        return placeScrapQueryRepository.getScrapedPlaceGrouping(user.getId());
    }

    /*
    유저별 스크랩 개수 변경으로 인한 Eviction
     */
    @CacheEvict(value = "scrap-count:user-id", key = "#createPlaceScrapRequestDto.id")
    @Transactional
    public void createScrap(CreatePlaceScrapRequestDto createPlaceScrapRequestDto) {

        Optional<Place> placeOpt = placeRepository.findAllByPosition(new Position(createPlaceScrapRequestDto.getLat(), createPlaceScrapRequestDto.getLng()));
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(createPlaceScrapRequestDto.getId());
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
