package cmc.mellyserver.domain.scrap;

import org.springframework.stereotype.Component;

import cmc.mellyserver.dbcore.scrap.PlaceScrapRepository;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PlaceScrapValidator {

    private final PlaceScrapRepository placeScrapRepository;

    public void validateDuplicatedScrap(Long userId, Long placeId) {
        boolean isDuplicated = placeScrapRepository.existsByUserIdAndPlaceId(userId, placeId);
        if (isDuplicated) {
            throw new BusinessException(ErrorCode.DUPLICATE_SCRAP);
        }
    }

    public void validateExistedScrap(Long userId, Long placeId) {

        boolean isDuplicated = placeScrapRepository.existsByUserIdAndPlaceId(userId, placeId);
        if (!isDuplicated) {
            throw new BusinessException(ErrorCode.NOT_EXIST_SCRAP);
        }
    }
}
