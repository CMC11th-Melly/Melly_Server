package cmc.mellyserver.mellyapi.common.aop.place;


import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.dbcore.place.PlaceRepository;
import cmc.mellyserver.dbcore.place.Position;
import cmc.mellyserver.mellyapi.domain.memory.dto.request.CreateMemoryRequestDto;
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

