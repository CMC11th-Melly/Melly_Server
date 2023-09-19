package cmc.mellyserver.mellycore.common.aop.place;

import cmc.mellyserver.mellycore.memory.application.dto.request.CreateMemoryRequestDto;
import cmc.mellyserver.mellycore.place.domain.Place;
import cmc.mellyserver.mellycore.place.domain.Position;
import cmc.mellyserver.mellycore.place.domain.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class CheckPlaceExistsAop {

    private final PlaceRepository placeRepository;

    @Around("@annotation(cmc.mellyserver.mellycore.common.aop.place.CheckPlaceExists) && args(createMemoryRequestDto)")
    public Object createPlaceWhenMemoryCreatedToNotExistPlace(final ProceedingJoinPoint joinPoint, final CreateMemoryRequestDto createMemoryRequestDto) throws Throwable {

        Optional<Place> place = placeRepository.findPlaceByPosition(new Position(createMemoryRequestDto.getLat(), createMemoryRequestDto.getLng()));

        if (place.isEmpty()) {
            Place savePlace = createMemoryRequestDto.toPlace();
            placeRepository.save(savePlace);
        }

        return joinPoint.proceed();
    }

}

