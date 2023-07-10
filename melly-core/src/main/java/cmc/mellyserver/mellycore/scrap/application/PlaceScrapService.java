package cmc.mellyserver.mellycore.scrap.application;


import cmc.mellyserver.mellycommon.codes.ErrorCode;
import cmc.mellyserver.mellycommon.enums.ScrapType;
import cmc.mellyserver.mellycore.common.AuthenticatedUserChecker;
import cmc.mellyserver.mellycore.common.exception.GlobalBadRequestException;
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
    캐싱 적용 가능 여부 : 불가능
    이유 : 반환 하는 DTO에 다른 사람이 해당 장소에 작성한 메모리 개수가 포함된다. 대규모 사용자가 유입될 시 자주 업데이트 되는 항목이므로 캐싱 효율성이 적다.
     */
    @Transactional(readOnly = true)
    public Slice<ScrapedPlaceResponseDto> findScrapedPlace(Pageable pageable, Long userSeq, ScrapType scrapType) {

        return placeScrapQueryRepository.getUserScrapedPlace(pageable, userSeq, scrapType);
    }

    /*
    캐싱 적용 가능 여부 : 가능
    */
    @Transactional(readOnly = true)
    public List<PlaceScrapCountResponseDto> countByPlaceScrapType(Long userSeq) {

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(userSeq);
        return placeScrapQueryRepository.getScrapedPlaceGrouping(user.getUserSeq());
    }

    @Transactional
    public void createScrap(CreatePlaceScrapRequestDto createPlaceScrapRequestDto) {

        Optional<Place> placeOpt = placeRepository.findPlaceByPosition(new Position(createPlaceScrapRequestDto.getLat(), createPlaceScrapRequestDto.getLng()));
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(createPlaceScrapRequestDto.getUserSeq());
        Place place = checkPlaceExist(createPlaceScrapRequestDto, placeOpt);
        placeScrapRepository.save(PlaceScrap.createScrap(user, place, createPlaceScrapRequestDto.getScrapType()));
    }

    @Transactional
    public void removeScrap(Long userSeq, Double lat, Double lng) {

        Place place = placeRepository.findPlaceByPosition(new Position(lat, lng)).orElseThrow(() -> {
            throw new GlobalBadRequestException(ErrorCode.NO_SUCH_PLACE);
        });
        checkExistScrap(userSeq, place.getId());
        placeScrapRepository.deleteByUserUserSeqAndPlacePosition(userSeq, new Position(lat, lng));
    }

    private void checkDuplicatedScrap(Long userSeq, Long placeId) {

        placeScrapRepository.findByUserUserSeqAndPlaceId(userSeq, placeId).ifPresent(x -> {
            throw new GlobalBadRequestException(ErrorCode.DUPLICATE_SCRAP);
        });
    }

    private void checkExistScrap(Long userSeq, Long placeId) {

        placeScrapRepository.findByUserUserSeqAndPlaceId(userSeq, placeId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ErrorCode.NOT_EXIST_SCRAP);
        });
    }

    private Place checkPlaceExist(CreatePlaceScrapRequestDto createPlaceScrapRequestDto, Optional<Place> placeOpt) {

        if (placeOpt.isEmpty()) {
            return placeRepository.save(createPlaceScrapRequestDto.toEntity());
        } else {
            checkDuplicatedScrap(createPlaceScrapRequestDto.getUserSeq(), placeOpt.get().getId());
            return placeOpt.get();
        }
    }
}
