package cmc.mellyserver.domain.scrap;

import org.springframework.stereotype.Component;

import cmc.mellyserver.dbcore.scrap.PlaceScrapRepository;
import cmc.mellyserver.support.exception.CommonException;
import cmc.mellyserver.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PlaceScrapValidator {

    private final PlaceScrapRepository placeScrapRepository;

    public void validateDuplicatedScrap(Long userId, Long placeId) {
        if (checkDuplicatedScrap(userId, placeId)) {
            throw new CommonException(ErrorCode.DUPLICATE_SCRAP);
        }
    }

    public void validateExistedScrap(Long userId, Long placeId) {
        if (!checkDuplicatedScrap(userId, placeId)) {
            throw new CommonException(ErrorCode.NOT_EXIST_SCRAP);
        }
    }

    private boolean checkDuplicatedScrap(Long userId, Long placeId) {
        return placeScrapRepository.existsByUserIdAndPlaceId(userId, placeId);
    }
}
