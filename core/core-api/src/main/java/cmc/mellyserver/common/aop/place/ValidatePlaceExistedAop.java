package cmc.mellyserver.common.aop.place;

import java.util.Optional;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.dbcore.place.PlaceRepository;
import cmc.mellyserver.domain.memory.dto.request.CreateMemoryRequestDto;
import cmc.mellyserver.domain.scrap.dto.request.CreatePlaceScrapRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class ValidatePlaceExistedAop {

	private final PlaceRepository placeRepository;

	@Around("@annotation(cmc.mellyserver.common.aop.place.ValidatePlaceExisted)")
	public Object createPlace(final ProceedingJoinPoint joinPoint) throws Throwable {

		Object[] args = joinPoint.getArgs();

		for (Object arg : args) {

			if (arg instanceof CreatePlaceScrapRequestDto) {

				log.info("AOP 실행 - CreatePlaceScrapRequestDto");
				Optional<Place> place = placeRepository.findByPosition(((CreatePlaceScrapRequestDto)arg).getPosition());

				if (place.isEmpty()) {
					Place savePlace = ((CreatePlaceScrapRequestDto)arg).toEntity();
					placeRepository.save(savePlace);
				}

				return joinPoint.proceed();
			}

			if (arg instanceof CreateMemoryRequestDto) {
				log.info("AOP 실행 - CreateMemoryRequestDto");
				Optional<Place> place = placeRepository.findByPosition(((CreateMemoryRequestDto)arg).getPosition());

				if (place.isEmpty()) {
					Place savePlace = ((CreateMemoryRequestDto)arg).toPlace();
					placeRepository.save(savePlace);
				}

				return joinPoint.proceed();
			}
		}

		return null;
	}

}
