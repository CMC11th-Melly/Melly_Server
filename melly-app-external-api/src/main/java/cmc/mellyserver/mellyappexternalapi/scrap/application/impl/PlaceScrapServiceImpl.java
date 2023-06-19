package cmc.mellyserver.mellyappexternalapi.scrap.application.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cmc.mellyserver.mellyappexternalapi.common.auth.AuthenticatedUserChecker;
import cmc.mellyserver.mellyappexternalapi.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.mellyappexternalapi.common.exception.GlobalBadRequestException;
import cmc.mellyserver.mellyappexternalapi.scrap.application.PlaceScrapService;
import cmc.mellyserver.mellyappexternalapi.scrap.application.dto.request.CreatePlaceScrapRequestDto;
import cmc.mellyserver.mellydomain.common.enums.ScrapType;
import cmc.mellyserver.mellydomain.place.domain.Place;
import cmc.mellyserver.mellydomain.place.domain.Position;
import cmc.mellyserver.mellydomain.place.domain.repository.PlaceQueryRepository;
import cmc.mellyserver.mellydomain.place.domain.repository.PlaceRepository;
import cmc.mellyserver.mellydomain.scrap.domain.PlaceScrap;
import cmc.mellyserver.mellydomain.scrap.domain.repository.PlaceScrapRepository;
import cmc.mellyserver.mellydomain.scrap.domain.repository.dto.PlaceScrapCountResponseDto;
import cmc.mellyserver.mellydomain.scrap.domain.repository.dto.ScrapedPlaceResponseDto;
import cmc.mellyserver.mellydomain.user.domain.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlaceScrapServiceImpl implements PlaceScrapService {

	private final PlaceRepository placeRepository;

	private final PlaceQueryRepository placeQueryRepository;

	private final PlaceScrapRepository scrapRepository;

	private final AuthenticatedUserChecker authenticatedUserChecker;

	@Override
	public Slice<ScrapedPlaceResponseDto> findScrapedPlace(Pageable pageable, Long userSeq, ScrapType scrapType) {
		return placeQueryRepository.getScrapedPlace(pageable, userSeq, scrapType);
	}

	@Override
	public List<PlaceScrapCountResponseDto> countByPlaceScrapType(Long userSeq) {
		User user = authenticatedUserChecker.checkAuthenticatedUserExist(userSeq);
		return placeQueryRepository.getScrapedPlaceGrouping(user);
	}

	@Override
	@Transactional
	public void createScrap(CreatePlaceScrapRequestDto createPlaceScrapRequestDto) {
		Optional<Place> placeOpt = placeRepository.findPlaceByPosition(
			new Position(createPlaceScrapRequestDto.getLat(), createPlaceScrapRequestDto.getLng()));
		User user = authenticatedUserChecker.checkAuthenticatedUserExist(createPlaceScrapRequestDto.getUserSeq());
		if (placeOpt.isEmpty()) {
			Place savePlace = placeRepository.save(Place.builder()
				.placeName(createPlaceScrapRequestDto.getPlaceName())
				.placeCategory(createPlaceScrapRequestDto.getPlaceCategory())
				.position(new Position(createPlaceScrapRequestDto.getLat(), createPlaceScrapRequestDto.getLng()))
				.build());
			scrapRepository.save(PlaceScrap.createScrap(user, savePlace, createPlaceScrapRequestDto.getScrapType()));
		} else {
			checkDuplicatedScrap(createPlaceScrapRequestDto.getUserSeq(), placeOpt.get().getId());
			scrapRepository.save(
				PlaceScrap.createScrap(user, placeOpt.get(), createPlaceScrapRequestDto.getScrapType()));
		}
	}

	@Override
	@Transactional
	public void removeScrap(Long userSeq, Double lat, Double lng) {
		Place place = placeRepository.findPlaceByPosition(new Position(lat, lng)).orElseThrow(() -> {
			throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_PLACE);
		});
		checkExistScrap(userSeq, place.getId());
		scrapRepository.deleteByUserUserSeqAndPlacePosition(userSeq, new Position(lat, lng));
	}

	private void checkDuplicatedScrap(Long userSeq, Long placeId) {
		scrapRepository.findByUserUserSeqAndPlaceId(userSeq, placeId)
			.ifPresent(x -> {
				throw new GlobalBadRequestException(ExceptionCodeAndDetails.DUPLICATE_SCRAP);
			});
	}

	private void checkExistScrap(Long userSeq, Long placeId) {
		scrapRepository.findByUserUserSeqAndPlaceId(userSeq, placeId)
			.orElseThrow(() -> {
				throw new GlobalBadRequestException(ExceptionCodeAndDetails.NOT_EXIST_SCRAP);
			});
	}
}
